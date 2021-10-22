import { Entity, ListResult } from './index';

export declare type SortOrder = 'descend' | 'ascend' | null;

/**
 * query方法的参数汇总到一起，保存到store中
 */
export interface QueryOptions<T extends Entity> {
  params: QueryParam<T>;
  sort: Record<string, SortOrder>;
  filter: Record<keyof T, any[]>;
}
export type QueryParam<T> = Partial<T> & {
  //分页信息
  pageSize?: number;
  current?: number;
  //通用关键字
  keyword?: string;
};
export type QueryResult<T> = ListResult<T> & {
  data?: T[];
  success?: boolean;
  total?: number;
};
export interface QueryApi<T extends Entity> {
  query(
    params: QueryParam<T>,
    sort: Record<string, SortOrder>,
    filter: Record<keyof T, any[]>,
  ): Promise<QueryResult<T>>;
}
