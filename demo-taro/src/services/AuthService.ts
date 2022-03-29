import { StoreService, AbstractClient, LoginInfo } from 'matrix-ui-service';

export class AuthStore {
  loginInfo: LoginInfo = { success: false };

  hasRole(role: string): boolean {
    const { roles } = this.loginInfo;
    if (!roles) return false;
    return roles.includes(role);
  }
  get isLogin() {
    return this.loginInfo.success;
  }
  get isAdmin(): boolean {
    return this.isLogin && this.hasRole('Administrators');
  }
}
export class AuthService extends StoreService<AuthStore> {
  constructor(restClient: AbstractClient) {
    super(restClient);
    this.store = new AuthStore();
  }

  afterLogin(loginInfo: LoginInfo) {
    this.setStore({ loginInfo });
  }

  /**
   * 暂时没有调用后台
   * @param operator
   */
  getApiUri(operator: string): string {
    return `/wechat/ma/${operator}`;
  }
}
