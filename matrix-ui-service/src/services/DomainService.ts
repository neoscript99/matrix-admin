import {
  Criteria,
  Entity,
  ListOptions,
  ListResult,
  PageInfo,
  StoreService,
  AbstractClient,
  LoginInfo,
  QueryApi,
  SortOrder,
  CriteriaOrder,
  QueryParam,
  QueryResult,
  AfterLoginService,
} from './';
import { DomainStore } from './DomainStore';
import { LangUtil, ServiceUtil, StringUtil } from '../utils';

export interface DomainServiceOptions<D extends DomainStore = DomainStore> {
  domain: string;
  initStore?: D;
  storeClass?: new () => D;
  restClient: AbstractClient;
}
/**
 * 领域业务基类
 */
export class DomainService<T extends Entity = Entity, D extends DomainStore<T> = DomainStore<T>>
  extends StoreService<D>
  implements QueryApi<T>, AfterLoginService
{
  public store: D;
  domain: string;

  /**
   *
   * @param domain
   * @param graphqlClient
   * @param dependStoreMap 依赖的其它store，格式如下：{aaStore:aa,bbStore:bb}
   */
  constructor(options: DomainServiceOptions<D>) {
    super(options.restClient);
    if (options.initStore) this.store = options.initStore;
    else if (options.storeClass) this.store = new options.storeClass();
    else throw 'DomainService.constructor need initStore or storeClass';

    this.domain = options.domain;
  }

  getApiUri(operator: string) {
    return `/api/${this.domain}/${operator}`;
  }

  findFirst(criteria?: Criteria) {
    const pageInfo = { pageSize: 1, currentPage: 1 };
    return this.list({ criteria, pageInfo }).then((data) =>
      this.changeCurrentItem(data.totalCount > 0 ? data.results[0] : ({} as T)),
    );
  }

  /**
   * 返回后设置 allList
   * @param criteria
   * @returns {Promise<{client: *, fields?: *}>}
   */
  listAll(options: ListOptions): Promise<ListResult<T>> {
    return this.list(options).then((data) => {
      this.store.allList = data.results;
      this.fireStoreChange();
      return data;
    });
  }

  /**
   * 不改变类成员变量
   * @param criteria,如果要保证order顺序，criteria中应该预先占位，例如：
   * const orders: CriteriaOrder[] = [['plan.planBeginDay', 'desc'], 'topicCateCode', 'initialCode', 'dept.seq'];
   * const criteria: Criteria = { plan: {}, order: [], dept: {} };
   * 如果要保证orders中的顺序，criteria需要plan、order和dept预先占位
   * @param pageInfo
   * @param orders
   * @returns {Promise<{client: *, fields?: *}>}
   */
  list({ criteria = {}, pageInfo, orders }: ListOptions): Promise<ListResult<T>> {
    const { maxResults, firstResult, order, ...countCriteria } = criteria;
    //先调用count，防止countCriteria被后面的步骤污染
    const countPromise = pageInfo ? this.count(countCriteria) : Promise.resolve(0);

    criteria.order = [...(criteria.order || []), ...(orders || [])];
    ServiceUtil.processNestCriteria(criteria);
    if (pageInfo) ServiceUtil.processCriteriaPage(criteria, pageInfo);
    const listPromise = this.postApi('list', criteria) as Promise<T[]>;
    if (pageInfo) {
      return Promise.all([listPromise, countPromise]).then(([results, totalCount]) => ({
        results,
        totalCount,
      }));
    } else {
      return listPromise.then((results) => ({ results, totalCount: results.length }));
    }
  }

  /**
   * 数据库count
   * 注意：嵌套属性中不能带入的order，否则报错
   * @param criteria
   */
  count(criteria: Criteria): Promise<number> {
    //count时不需要任何order信息，如果是嵌套的order还会报错
    const { maxResults, firstResult, order, ...countCriteria } = criteria;
    ServiceUtil.processNestCriteria(countCriteria);
    return this.postApi('count', countCriteria) as Promise<number>;
  }

  /**
   * 返回后设置 pageInfo pageList allList
   *
   *
   * @param {boolean} isAppend
   * @param {PageInfo} pageInfo 此处传入pageInfo优先，以store.pageInfo为准
   * @param {{criteria?: Criteria; orders?: CriteriaOrder[]}} rest
   * @returns {Promise<ListResult>}
   */
  listPage({ isAppend = false, pageInfo, ...rest }: ListOptions): Promise<ListResult<T>> {
    //查询第一页的时候，清空allList
    if (pageInfo) this.store.pageInfo = pageInfo;
    if (this.store.pageInfo.currentPage === 1) this.store.allList = [];
    return this.list({ pageInfo: this.store.pageInfo, ...rest }).then((data) => {
      const { results, totalCount } = data;
      this.store.pageList = results;
      this.store.pageInfo.totalCount = totalCount;
      this.store.pageInfo.isLastPage =
        results.length < this.store.pageInfo.pageSize ||
        this.store.pageInfo.pageSize * this.store.pageInfo.currentPage >= totalCount;
      if (isAppend === true) this.store.allList = this.store.allList.concat(results);
      else this.store.allList = results;
      this.fireStoreChange();
      return data;
    });
  }

  listNextPage(param: ListOptions): string | Promise<ListResult<T>> {
    if (this.store.pageInfo.isLastPage) return '已经到底了';
    else {
      this.store.pageInfo.currentPage++;
      return this.listPage(param);
    }
  }

  listFirstPage(param: ListOptions): Promise<ListResult<T>> {
    this.store.pageInfo.currentPage = 1;
    return this.listPage(param);
  }

  clearList() {
    this.store.pageList = [];
    this.store.allList = [];
    this.fireStoreChange();
  }

  changeCurrentItem(currentItem: T): T {
    this.store.currentItem = currentItem;
    this.fireStoreChange();
    return currentItem;
  }

  /**
   * create or update,根据item.id是否存在判断
   * @param newItem
   */
  save(item: Partial<T>): Promise<T> {
    return this.postApi('save', item).then((data) => {
      const newItem = data as T;
      if (!item.id) this.newListItem(newItem);
      else this.updateListItem(newItem);
      return this.changeCurrentItem(newItem);
    });
  }

  get(id: any): Promise<T> {
    return this.postApi('get', { id }).then((data) => this.changeCurrentItem(data as T));
  }

  delete(id: any): Promise<number> {
    return this.postApi('delete', { id }).then((data) => {
      this.deleteListItems([id]);
      return data;
    });
  }

  deleteByIds(ids: any[]): Promise<any> {
    return this.postApi('deleteByIds', { ids }).then((data) => {
      this.deleteListItems(ids);
      return data;
    });
  }

  syncPageInfo(newPageInfo: PageInfo) {
    Object.assign(this.store.pageInfo, newPageInfo);
  }

  get packageName() {
    return '';
  }

  get readAuthorities() {
    const name = StringUtil.upperFirst(this.domain);
    const pName = this.packageName;
    return ['SysAdmin', 'SysRead', `${pName}PackageAll`, `${pName}PackageRead`, `${name}All`, `${name}Read`];
  }

  readAuthorize(hasList?: string[]): boolean {
    const needOneList = this.readAuthorities;
    const has = !!hasList?.find((au) => needOneList.includes(au));
    if (!has)
      console.warn(
        `${LangUtil.getClassName(this)}.readAuthorize不通过: 当前用户(${hasList})无其中任一权限${needOneList}`,
      );
    return has;
  }

  /**
   * 更新一个对象后，更新列表中对应项
   * @param item
   */
  updateListItem(item: T) {
    const { allList, pageList } = this.store;
    this.store.allList = allList.map((v) => (v.id === item.id ? item : v));
    this.store.pageList = pageList.map((v) => (v.id === item.id ? item : v));
  }

  /**
   * 新增的放到列表最前
   * @param item
   */
  newListItem(item: T) {
    const { allList, pageList } = this.store;
    this.store.allList = [item, ...allList];
    this.store.pageList = [item, ...pageList];
  }

  deleteListItems(ids: any[]) {
    const { allList, pageList } = this.store;
    this.store.allList = allList.filter((item) => !ids.includes(item.id));
    this.store.pageList = pageList.filter((item) => !ids.includes(item.id));
  }

  afterLogin(loginInfo: LoginInfo) {
    this.store.needRefresh = true;
  }

  setCurrentItem(newItem: Partial<T>) {
    this.changeCurrentItem({ ...this.store.currentItem, ...newItem });
  }

  /**
   * 配合前台（antd）查询功能， 支持嵌套属性
   * pageInfo已处理
   * @param params 默认后模糊like查询
   * @param sort 已处理
   * @param filter 默认in查询
   */
  query(
    params: QueryParam<T>,
    sort: Record<string, SortOrder>,
    filter: Record<keyof T, any[]>,
  ): Promise<QueryResult<T>> {
    //保存查询信息，可做页面状态恢复
    this.store.queryOptions = { params, sort, filter };
    const { keyword, current, pageSize, ...queryParam } = params;
    const criteria: Criteria = { eq: [], like: [], inList: [], order: [] };
    this.processQueryParams(queryParam as Partial<T>, criteria);
    this.processKeyword(keyword, criteria);
    this.processQuerySort(sort, criteria);
    this.processQueryFilter(filter, criteria);
    const pageInfo: PageInfo = current && { currentPage: current, pageSize: pageSize || 10 };
    const options = { criteria, pageInfo };
    //console.debug('DomainService.query: ', JSON.stringify(options));
    //返回结果满足antd-pro的request
    return this.listPage(options).then((res) => ({ ...res, success: true, total: res.totalCount, data: res.results }));
  }

  /**
   * params是嵌套结构，需要先拉平
   * sort 和 filter的key是逗号份额
   */
  processQueryParams(params: Partial<T>, criteria: Criteria) {
    const likes = criteria.like;
    for (const [k, v] of Object.entries(LangUtil.flattenObject(params))) {
      likes.push([k, v + '%']);
    }
  }
  //如果是通用关键字查询，需继承类自行处理
  processKeyword(keyword: string, criteria: Criteria) {
    return criteria;
  }
  processQuerySort(sort: Record<string, SortOrder>, criteria: Criteria) {
    const orders = criteria.order;
    for (const [k, v] of Object.entries(sort)) {
      orders.push([k.replace(',', '.'), v === 'descend' ? 'desc' : 'asc']);
    }
  }
  processQueryFilter(filter: Record<keyof T, any[]>, criteria: Criteria) {
    const inLists = criteria.inList;

    for (const [k, v] of Object.entries(filter)) {
      //如果没有选择v为null
      v && inLists.push([k.replace(',', '.'), v]);
    }
  }
}
