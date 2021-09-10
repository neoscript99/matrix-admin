import { LoginService, StoreService, AbstractClient } from './index';
import { UserBindRes } from './UserBindService';

export interface WxMaUser {
  /** 用户头像图片的 URL。URL 最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640x640 的正方形头像，46 表示 46x46 的正方形头像，剩余数值以此类推。默认132），用户没有头像时该项为空。若用户更换头像，原有头像 URL 将失效。 */
  avatarUrl: string;
  /** 用户所在城市 */
  city: string;
  /** 用户所在国家 */
  country: string;
  /** 用户性别 */
  gender: string;
  /** 显示 country，province，city 所用的语言 */
  language: string;
  /** 用户昵称 */
  nickName: string;
  /** 用户所在省份 */
  province: string;
  openId?: string;
  unionId?: string;
}
export class WxMaService extends StoreService<UserBindRes> {
  constructor(restClient: AbstractClient, private loginService: LoginService) {
    super(restClient);
    this.store = { loginInfo: { success: false } };
  }

  getApiUri(operator: string): string {
    return `/wechat/ma/${operator}`;
  }

  bindUser(userInfo: WxMaUser) {
    const { userBind } = this.store;
    if (!userBind?.openid) {
      const reason = '微信openId未获取，请先调用WxMaService.wxMaLogin';
      console.error(reason);
      return Promise.reject(reason);
    }
    const wxMaUser = { ...userInfo, openId: userBind.openid, unionId: userBind.unionid };
    return this.postApi('bindUser', wxMaUser).then(this.afterBind);
  }

  bindPhone(code, encryptedData, ivStr) {
    return this.postApi('bindPhone', {
      code: code,
      encryptedData: encryptedData,
      ivStr: ivStr,
    }).then(this.afterBind);
  }

  wxMaLogin(code: string) {
    // 发送 res.code 到后台换取 openId, sessionKey, unionId
    return this.postApi('wxMaLogin', { code }).then(this.afterBind);
  }

  afterBind = (bindRes: UserBindRes) => {
    this.loginService.doAfterLogin(bindRes.loginInfo);
    this.setStore(bindRes);
    return bindRes;
  };
}
