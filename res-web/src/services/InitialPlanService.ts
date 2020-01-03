import { AbstractClient, DomainService, MobxDomainStore, Entity, DictInitService } from 'oo-rest-mobx';
import { observable } from 'mobx';

export class InitialPlanStore extends MobxDomainStore {
  @observable
  startedList?: Entity[];
}
export class InitialPlanService extends DomainService<InitialPlanStore> implements DictInitService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'initialPlan', storeClass: InitialPlanStore, restClient });
  }
  listStarted() {
    return this.restClient.post(this.getApiUri('listStarted')).then(res => ((this.store.startedList = res), res));
  }
  initDictList() {
    this.listStarted();
  }
}
