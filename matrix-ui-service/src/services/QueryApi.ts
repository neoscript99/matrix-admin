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
export interface QueryPage {
  pageSize?: number;
  current?: number;
}
export type QueryParam<T> = Partial<T> &
  QueryPage & {
    //通用关键字
    keyword?: string;
  };
export interface QueryApi<T extends Entity> {
  query(params: QueryParam<T>, sort: Record<string, SortOrder>, filter: Record<string, any[]>): Promise<ListResult<T>>;
}
