import { LoginInfo, AdminServices, SpringBootClient } from 'oo-rest-mobx';
import { config } from '../utils';

export const restClient = new SpringBootClient({ rootUrl: config.serverRoot });

export const adminServices = new AdminServices(restClient, afterLogin);

function afterLogin(loginInfo: LoginInfo) {
  if (!loginInfo.token) throw 'token不能为空';
}

export const userService = adminServices.userService;
userService.trySessionLogin().then(loginInfo => loginInfo.success || userService.tryLocalLogin());
