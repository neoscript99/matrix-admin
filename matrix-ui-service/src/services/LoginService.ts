import { ResBean, StoreService, UserEntity } from './index';
import { SpringBootClient, SpringErrorHandler } from '../rest';
import { StringUtil, MessageUtil, StorageUtil, ObjectUtil } from '../utils';

/**
 * 如果是系统自己认证：user 有 ， account 有
 * 如果接入了cas认证：account 有， user 不一定有
 */
export interface LoginInfo {
  success: boolean;
  token?: string;
  user?: UserEntity;
  account?: string;
  error?: string;
  roles?: string[];
  authorities?: string[];
  kaptchaFree?: boolean;
}

export class LoginStore {
  //clientEnabled默认为true，不显示登录框，后台查询如果为false，再显示出来
  loginInfo: LoginInfo = { success: false };
  forcePasswordChange = false;
}

/**
 * 在登录后将被执行
 * 数据依赖通过StoreService.fireStoreChange处理
 */
export interface AfterLogin {
  (loginInfo?: LoginInfo): Promise<any> | void;
}
export interface AfterLoginService {
  afterLogin: AfterLogin;
}

export interface LoginEntity {
  username: string;
  password: string;
  passwordHash?: string;
  kaptchaCode?: string;
  remember?: boolean;
  isDev?: boolean;
}
const USERNAME_KEY = 'loginUsername';
const PASSWORD_KEY = 'loginPassword';
const LOGOUT_ERRORS = ['NoUser', 'NoToken'];

/**
 * afterLogins初始化后不能为null，可以是[]
 */
export class LoginService extends StoreService<LoginStore> {
  store = new LoginStore();
  //用户的初始化密码，可在new LoginService的时候修改
  //如果用户密码登录初始密码，跳转到密码修改页面
  initPassword = 'abc000';
  private afterLogins: AfterLogin[] = [];

  constructor(restClient: SpringBootClient) {
    super(restClient);
    restClient.registerErrorHandler(this.springErrorHandler);
  }

  springErrorHandler: SpringErrorHandler = (e) => {
    if (e.errorCode && LOGOUT_ERRORS.includes(e.errorCode)) {
      console.error('登录信息错误：', e.errorCode, e.message);
      this.logout();
    }
  };

  getApiUri(operator: string) {
    return `/api/login/${operator}`;
  }

  get kaptchaRenderURL() {
    return this.restClient.fetchOptions.rootUrl + this.getApiUri('kaptcha');
  }
  kaptchaFree(username: string): Promise<ResBean> {
    return this.postApi('kaptchaFree', { username });
  }

  login(loginEntity: LoginEntity): Promise<LoginInfo> {
    return this.loginHash({ ...loginEntity, passwordHash: StringUtil.sha256(loginEntity.password) });
  }

  loginHash({ username, passwordHash, kaptchaCode, remember, isDev }: LoginEntity): Promise<LoginInfo> {
    this.clearLoginInfoLocal();
    return this.postApi(isDev ? 'devLogin' : 'login', { username, passwordHash, kaptchaCode }).then((loginInfo) => {
      if (loginInfo.success) {
        //如果密码等于初始密码，强制修改
        if (passwordHash === this.initPasswordHash) {
          this.store.forcePasswordChange = true;
          MessageUtil.warn('请修改初始密码');
        }
        if (remember) this.saveLoginInfoLocal(username, passwordHash!);
      } else MessageUtil.error(loginInfo.error);
      this.doAfterLogin(loginInfo);
      return loginInfo;
    });
  }

  tryLocalLogin() {
    const { username, password } = this.getLoginInfoLocal();
    if (username && password) this.loginHash({ username, password, passwordHash: password, remember: true });
  }

  saveLoginInfoLocal(username: string, password: string) {
    StorageUtil.setItem(USERNAME_KEY, username);
    StorageUtil.setItem(PASSWORD_KEY, password);
  }

  clearLoginInfoLocal() {
    StorageUtil.removeItem(USERNAME_KEY);
    StorageUtil.removeItem(PASSWORD_KEY);
    this.store.loginInfo = { success: false };
    this.fireStoreChange();
  }

  getLoginInfoLocal() {
    return {
      username: StorageUtil.getItem(USERNAME_KEY),
      password: StorageUtil.getItem(PASSWORD_KEY),
    };
  }

  trySessionLogin(): Promise<LoginInfo> {
    return this.postApi('sessionLogin').then((data) => {
      this.doAfterLogin(data);
      return data;
    });
  }

  logout = (history?: any) => {
    this.clearLoginInfoLocal();
    //丢弃Token
    ObjectUtil.set(this.restClient.fetchOptions, 'reqInit.headers.Authorization', `Bearer anonymous`);
    this.postApi('logout');
    history?.push && history.push('/');
  };

  doAfterLogin(loginInfo: LoginInfo) {
    if (loginInfo.success) {
      if (!loginInfo.token) throw 'token不能为空';
      //必须最先执行，否则验证错误
      ObjectUtil.set(this.restClient.fetchOptions, 'reqInit.headers.Authorization', `Bearer ${loginInfo.token}`);
      //cas登录，可以不要求必须存在数据库user
      if (!loginInfo.user)
        loginInfo.user = { account: loginInfo.account || '', dept: { name: '外部临时用户', seq: 0, enabled: true } };
      //并行执行，不依赖执行顺序
      this.afterLogins.forEach((fun) => fun(loginInfo));
    } else {
      console.debug('LoginService.doAfterLogin: ', loginInfo.error);
    }
    //store更新，触发登录界面刷新
    this.store.loginInfo = loginInfo;
    this.fireStoreChange();
  }

  hasRole(role: string): boolean {
    const { loginInfo } = this.store;
    if (loginInfo.roles) return loginInfo.roles.includes(role);
    else return false;
  }

  get user() {
    return this.store.loginInfo?.user;
  }

  get dept() {
    return this.store.loginInfo?.user?.dept;
  }

  get initPasswordHash() {
    return StringUtil.sha256(this.initPassword);
  }

  addAfterLogin(fun: AfterLogin) {
    this.afterLogins.push(fun);
  }

  updateLoginInfo(newInfo: Partial<LoginInfo>) {
    this.store.loginInfo = { ...this.store.loginInfo, ...newInfo };
    //保存一次肯定不需要再强制修改密码
    this.store.forcePasswordChange = false;
    this.fireStoreChange();
  }
}
