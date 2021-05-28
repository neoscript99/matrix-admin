import { StoreService, LoginInfo, LoginService } from './index';
import { SpringBootClient } from '../rest';

export interface WechatStore {
  qrcodeInfo?: QrcodeRes;
  //检查次数限制
  checkTimesUp?: boolean;
}

export interface QrcodeRes {
  //微信二维码
  wxImgUrl: string;
  //获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
  ticket: string;
  //该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。
  expire_seconds: string;
  //二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
  url: string;
  //场景值一般为随机字符串，需要返回前台
  scene_str: string;
  //LocalDateTime,默认格式字符串
  createTime: string;
  error: string;
}
export class WechatService extends StoreService<WechatStore> {
  store: WechatStore = {};
  timer;
  checkTimes = 60;

  constructor(restClient: SpringBootClient, private loginService: LoginService, private interval: number = 3000) {
    super(restClient);
    loginService.addAfterLogin(this.afterLogin);
  }

  getApiUri(operator: string): string {
    return `/wechat/mp/${operator}`;
  }

  /**
   * 第一步：获取二维码，并展示给客户端
   *
   * checkTimes：轮询次数，二维码展示给用户后，需等待扫码并轮询后台
   */
  getQrcodeInfo(checkTimes = 60): Promise<QrcodeRes | void> {
    if (this.loginService.store.loginInfo.success) return Promise.resolve();
    else
      return this.postApi('qrcode').then((res) => {
        this.store.qrcodeInfo = res;
        this.checkTimes = checkTimes;
        this.store.checkTimesUp = false;
        this.startBindCheck();
        this.fireStoreChange();
        return res;
      });
  }

  /**
   * 第二步：启动轮询任务
   */
  startBindCheck() {
    if (this.timer) clearInterval(this.timer);
    this.timer = setInterval(this.runBindCheck, this.interval);
  }

  /**
   * 第三步：检查用户是否已扫描
   * 二维码对应scene_str，用户扫码成功后，后台应返回登录信息
   * 用闭包，不用bind this
   */
  runBindCheck = () => {
    const { scene_str, error } = this.store.qrcodeInfo;
    if (scene_str && this.checkTimes > 0)
      this.postApi('checkBind', { scene_str }).then((res: LoginInfo) => {
        res.success && this.loginService.doAfterLogin(res);
      });
    else console.log('WechatService.runBindCheck: ', this.checkTimes, error);
    this.checkTimes--;
    if (this.checkTimes === 0) {
      this.stopBindCheck();
    }
  };

  /**
   * 第四步：停止轮询
   */
  stopBindCheck() {
    this.store.checkTimesUp = true;
    if (this.timer) clearInterval(this.timer);
    this.fireStoreChange();
  }

  /**
   * 登录成功后，清理定时器
   * 考虑到其它途径登录的场景，所以在构造函数中传递给loginService
   * @param loginInfo
   */
  afterLogin = () => {
    this.stopBindCheck();
  };
}
