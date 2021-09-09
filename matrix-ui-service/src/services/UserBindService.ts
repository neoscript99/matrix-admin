import { LoginInfo, Entity, UserEntity, AbstractClient, DomainStore, DomainService } from './index';

export interface UserBindEntity extends Entity {
  user: UserEntity;
  openid: string;
  unionid: string;
  //不能为空，user.name需要
  nickname: string;
  headimgurl: string;
  //	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
  sex: string;
  //用户所在城市
  city: string;
  //用户所在国家
  country: string;
  //用户所在省份
  province: string;
  //通过微信认证或短信认证的电话号码
  phoneNumber: string;
  //wechat, dingtalk等
  source: string;
}

export interface UserBindRes {
  loginInfo: LoginInfo;
  userBind?: UserBindEntity;
}
export class UserBindService extends DomainService<UserBindEntity> {
  constructor(restClient: AbstractClient) {
    super({ domain: 'userBind', storeClass: DomainStore, restClient });
  }
}
