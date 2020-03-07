import { AbstractClient, DomainService, MobxDomainStore, LoginInfo } from 'oo-rest-mobx';

export class ResDeptTypeService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'resDeptType', storeClass: MobxDomainStore, restClient });
  }
  afterLogin = (loginInfo: LoginInfo) => {
    return this.readAuthorize(loginInfo.authorities) ? this.listAll({}) : Promise.resolve();
  };
}
