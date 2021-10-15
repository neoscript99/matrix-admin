import { DEFAULT_PAGE_INFO, Entity, PageInfo, QueryOptions } from './index';

/**
 * Service Store基类，支持react hooks获取
 * 内部的属性会被JSON.stringify序列化，如果是嵌套结构或大对象，可以用Promise包装，规避序列化
 */
export class DomainStore<T extends Entity = Entity> {
  pageInfo: PageInfo = DEFAULT_PAGE_INFO;
  currentItem: T = {} as T;
  allList: T[] = [];
  pageList: T[] = [];
  needRefresh = true;
  searchParam: any = {};
  queryOptions?: QueryOptions<T>;
  [key: string]: any;
}
