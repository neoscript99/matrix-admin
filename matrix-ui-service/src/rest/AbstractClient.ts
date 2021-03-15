import { SomeFetch } from './index';

export interface FetchOptions {
  rootUrl: string;
  reqInit?: RequestInit;
  fetch?: SomeFetch;
}

export abstract class AbstractClient {
  fetch: SomeFetch;

  protected constructor(public fetchOptions: FetchOptions) {
    this.fetch = fetchOptions.fetch || window.fetch.bind(window);
  }

  /**
   * url = rootUrl + uri
   * @param uri
   * @param init
   */
  doFetch(uri: string, req?: RequestInit): Promise<Response> {
    const { rootUrl, reqInit } = this.fetchOptions;
    return this.fetch(rootUrl + uri, { ...reqInit, ...req });
  }

  /**
   * 如果data为string类型，JSON.stringify会加上双引号
   * 后台服务应该采用consumes = MediaType.APPLICATION_JSON_VALUE，才能转为正确的String
   * 否则这会变成""str""，多一个双引号
   *
   * 基于以上原因，最好还是不要直接传string作为参数
   * @param uri
   * @param data
   */
  post(uri: string, data?: any): Promise<any> {
    if (typeof data === 'string') throw 'string类型做json序列化和反序列化会有问题';
    return (
      this.doFetch(uri, data && { body: JSON.stringify(data) })
        //返回为空时，如果直接调用res.json()报错，所以先拿到text
        .then((res) => res.text())
        .then((text) => text && JSON.parse(text))
    );
  }
}
