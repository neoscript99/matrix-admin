import { AbstractClient, DomainService, MobxDomainStore, Entity, DictInitService } from 'oo-rest-mobx';
import { observable } from 'mobx';

export class PlanStore extends MobxDomainStore {
  @observable
  startedList?: Entity[];
}
export class PlanService extends DomainService<PlanStore> implements DictInitService {
  constructor(domain: string, restClient: AbstractClient) {
    super({ domain, storeClass: PlanStore, restClient });
  }
  listStarted() {
    return this.postApi('listStarted').then(res => ((this.store.startedList = res), res));
  }
  initDictList() {
    this.listStarted();
  }
}
