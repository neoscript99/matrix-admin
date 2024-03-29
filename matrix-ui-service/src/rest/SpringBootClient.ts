import { AbstractClient, FetchOptions } from './AbstractClient';
import { MessageUtil } from '../utils';

export interface SpringError {
  error: string;
  errorCode?: string;
  message: string;
  timestamp: string;
  status: number;
  exception: string;
  path: string;
}
export type SpringErrorHandler = (e: SpringError) => void;
//默认的错误处理
const defaultErrorHandler: SpringErrorHandler = (error) => MessageUtil.error('错误信息：' + error.message);
export class SpringBootClient extends AbstractClient {
  errorHandlers: SpringErrorHandler[];
  constructor(fetchOptions: FetchOptions) {
    const { reqInit } = fetchOptions;
    const headers = {
      'Content-Type': 'application/json',
      Authorization: 'Bearer anonymous',
    };
    const req: RequestInit = {
      method: 'POST',
      mode: 'cors',
      headers,
      credentials: 'include',
      ...reqInit,
    };
    super({
      ...fetchOptions,
      reqInit: req,
    });
    this.errorHandlers = [defaultErrorHandler];
  }

  registerErrorHandler(handler: SpringErrorHandler) {
    this.errorHandlers.push(handler);
  }
  /**
   * springboot的fetch不抛出异常，res.ok为false
   */
  doFetch(uri: string, req?: RequestInit): Promise<Response> {
    return new Promise((resolve, reject) =>
      super.doFetch(uri, req).then((res) => {
        if (res.ok) {
          resolve(res);
          return;
        }
        console.error('SpringBootClient.doFetch: ', res);
        res.text().then((text) => {
          try {
            const error: SpringError = JSON.parse(text);
            for (const handler of this.errorHandlers) handler(error);
            console.error('SpringBootClient.doFetch: ', error);
            reject(error.message);
          } catch (e) {
            console.error('SpringBootClient.doFetch: ', text, e);
            reject(text);
          }
        });
      }),
    );
  }
}
