import { StoreService } from './StoreService';
import { AfterLogin, LoginInfo, LoginService } from './LoginService';
import { SpringBootClient } from '../rest';

export interface WechatStore {
  qrcodeInfo?: QrcodeRes;
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
}
export class WechatService extends StoreService<WechatStore> {
  store: WechatStore = {};
  timer;

  constructor(restClient: SpringBootClient, private loginService: LoginService, private interval: number = 3000) {
    super(restClient);
    loginService.addAfterLogin(this.afterLogin);
  }

  getApiUri(operator: string): string {
    return `/wechat/${operator}`;
  }

  /**
   * 第一步：获取二维码，并展示给客户端
   */
  getQrcodeInfo(): Promise<QrcodeRes> {
    return this.postApi('qrcode').then((res) => {
      this.store.qrcodeInfo = res;
      this.startLoginCheck();
      this.fireStoreChange();
      return res;
    });
  }

  startLoginCheck() {
    this.stopLoginCheck();
    this.timer = setInterval(this.runLoginCheck, this.interval);
  }

  /**
   * 用闭包，不用bind this
   */
  runLoginCheck = () => {
    const sceneStr = this.store?.qrcodeInfo.scene_str;
    if (sceneStr)
      this.postApi('checkLogin', sceneStr).then((res: LoginInfo) => {
        this.loginService.doAfterLogin(res);
      });
  };

  stopLoginCheck() {
    if (this.timer) clearInterval(this.timer);
  }

  /**
   * 登录成功后，清理定时器
   * 考虑到其它途径登录的场景，所以在构造函数中传递给loginService
   * @param loginInfo
   */
  afterLogin = (loginInfo: LoginInfo) => {
    this.stopLoginCheck();
    return Promise.resolve();
  };
}
