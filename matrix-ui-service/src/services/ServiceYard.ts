import { AdminServices, DomainService, LoginInfo, SpringBootClient } from './';
export interface ServiceYardOption {
  restClient: SpringBootClient;
  initServices?: Partial<AdminServices>;
}
export function getDomainServices(container: any): DomainService[] {
  return Object.values(container).filter((s) => s instanceof DomainService) as DomainService[];
}
export class ServiceYard {
  adminServices: AdminServices;
  constructor(option: ServiceYardOption) {
    this.adminServices = new AdminServices(option.restClient, option.initServices);
    const { loginService } = this.adminServices;
    loginService.addAfterLogin(this.afterLogin);
    //session登录不成功的话，尝试本地保存的用户密码登录
    loginService.trySessionLogin().then((loginInfo) => loginInfo.success || loginService.tryLocalLogin());
  }

  afterLogin = (loginInfo: LoginInfo) => {
    getDomainServices(this.adminServices)
      .concat(getDomainServices(this))
      .forEach((s) => s.afterLogin(loginInfo));
    this.adminServices.wxMpService.afterLogin();
  };
}
