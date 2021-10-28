import { AdminServices, AfterLoginService, LoginInfo, SpringBootClient } from './';
export interface ServiceYardOption {
  restClient: SpringBootClient;
  initServices?: Partial<AdminServices>;
}
//找到所有带afterLogin的属性
export function getAfterLoginServices(container: any): AfterLoginService[] {
  return Object.values(container).filter((s: any) => !!s.afterLogin) as AfterLoginService[];
}
export class ServiceYard {
  [key: string]: any;
  adminServices: AdminServices;
  constructor(option: ServiceYardOption) {
    this.adminServices = new AdminServices(option.restClient, option.initServices);
    const { loginService } = this.adminServices;
    loginService.addAfterLogin(this.afterLogin);
    //session登录不成功的话，尝试本地保存的用户密码登录
    loginService.trySessionLogin().then((loginInfo) => loginInfo.success || loginService.tryLocalLogin());
  }

  afterLogin = (loginInfo: LoginInfo) => {
    getAfterLoginServices(this.adminServices)
      .concat(getAfterLoginServices(this))
      .forEach((s) => s.afterLogin(loginInfo));
  };
}
