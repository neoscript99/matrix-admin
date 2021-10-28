//node-fetch@3 直接依赖src，import语法，jest不支持
import fetch from 'node-fetch';
import { SpringBootClient } from '../src/rest';
import { ServiceYard } from '../src/services';

const rootUrl = 'http://localhost:8080';
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
export const restClient = new SpringBootClient({ rootUrl, fetch });

export const yard = new ServiceYard({ restClient });
//通过await loginPromise;等待登录完成
export const loginPromise = yard.adminServices.loginService.login({ username: 'admin', isDev: true, password: '' });

export function sleep(seconds: number) {
  return new Promise((resolve) => setTimeout(resolve, seconds * 1000));
}
