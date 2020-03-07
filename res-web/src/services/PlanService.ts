import { AbstractClient, DomainService, MobxDomainStore, Entity, LoginInfo } from 'oo-rest-mobx';
import { observable } from 'mobx';

export class PlanStore extends MobxDomainStore {
  @observable
  startedList?: Entity[];
  @observable
  expandedRowKeys: string[] = [];
}
export class PlanService extends DomainService<PlanStore> {
  constructor(domain: string, restClient: AbstractClient) {
    super({ domain, storeClass: PlanStore, restClient });
  }
  listStarted() {
    return this.postApi('listStarted').then(res => ((this.store.startedList = res), res));
  }
  afterLogin = (loginInfo: LoginInfo) => {
    return this.readAuthorize(loginInfo.authorities) ? this.listStarted() : Promise.resolve();
  };
  get packageName() {
    return 'res';
  }
}
