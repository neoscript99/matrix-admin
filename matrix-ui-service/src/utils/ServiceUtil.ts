import { Criteria, CriteriaOrder, PageInfo, SomeFetch, StoreService } from '../services';

interface ReactType {
  useState: <S>(initialState: S) => [S, (prevState: S) => S];
  useEffect: (effect: () => any, deps?: ReadonlyArray<any>) => void;
}
export class ServiceUtil {
  /**
   * 通过本方法和react解绑
   */
  static initReactUseStore(react: ReactType) {
    const useServiceStore = <D>(service: StoreService<D>) => {
      const [store, setStore] = react.useState(service.store);
      react.useEffect(() => {
        service.addChangeListener(setStore);
        return () => service.removeChangeListener(setStore);
      }, [service]);
      return store;
    };
    return useServiceStore;
  }
  /**
   * 通过本方法和taro解绑
   * Taro在小程序中支持cookie
   * https://www.jianshu.com/p/2663ac10d471
   */
  static initTaroFetch(taroRequest: any): SomeFetch {
    const taroFetch: SomeFetch = (url: RequestInfo, { body: data, ...fetchOptions }: any) => {
      return taroRequest({
        url,
        data,
        header: fetchOptions.headers,
        dataType: 'txt',
        responseType: 'text',
        ...fetchOptions,
      }).then((res: any) => {
        res.ok = res.statusCode < 400;
        res.text = () => Promise.resolve(res.data);
        return res;
      });
    };
    return taroFetch;
  }
  /**
   * 转换为gorm的分页模式
   *
   * @param criteria
   * @param currentPage
   * @param pageSize
   * @see org.grails.datastore.gorm.query.criteria.AbstractDetachedCriteria
   */
  static processCriteriaPage(criteria: Criteria, pageInfo: PageInfo): Criteria {
    //AbstractDetachedCriteria中的分页函数为max和offset
    criteria.maxResults = pageInfo.pageSize;
    criteria.firstResult = (pageInfo.currentPage - 1) * pageInfo.pageSize;
    return criteria;
  }

  /**
   * 将字符串嵌套排序字段转化为gorm可处理的格式
   * 注意：如果多个排序字段包含嵌套属性，criteria应该先占好位置，嵌套属性可以为空
   *  如：const orders: CriteriaOrder[] = [['plan.planBeginDay', 'desc'], 'plan.id', 'dept.seq', 'initialCode']
   *  因按照plan -> dept顺序先初始化：const criteria: Criteria = {plan: {}, dept: {}};
   * @param param 传入是为了在原参数上做增量修改，如:
   *  processOrderParam({user:{eq:[['name','admin']]}},[['user.age','desc']])=>{user:{eq:[['name','admin']],order:[['age','desc']]}}
   * @param orders
   */
  static processCriteriaOrder(criteria: Criteria, orders: CriteriaOrder[]) {
    //嵌套字段的排序criteria
    const orderList = orders.reduce((notNestOrders, order) => {
      if (typeof order === 'string') order = [order, 'asc'];
      if (order[0].indexOf('.') === -1) notNestOrders.push(order);
      else {
        //['user.age','desc']=>['user','age']
        const nestFields: string[] = order[0].split('.');
        //order = ['age','desc']
        order[0] = nestFields[nestFields.length - 1];

        let parentParam = criteria;
        nestFields.slice(0, -1).forEach((field) => {
          if (!parentParam[field]) parentParam[field] = {};
          parentParam = parentParam[field] as Criteria;
        });

        if (parentParam.order) parentParam.order.push(order);
        else parentParam.order = [order];
      }
      return notNestOrders;
    }, [] as CriteriaOrder[]);
    if (orderList.length > 0) criteria.order = orderList;
  }

  static clearEntity(entity: any, ...deleteProps: string[]) {
    const { id, lastUpdated, dateCreated, version, errors, ...rest } = entity;
    deleteProps.every((prop) => delete rest[prop]);
    return rest;
  }
}
