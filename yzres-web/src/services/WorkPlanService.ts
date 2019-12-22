import { AbstractClient, DomainService, MobxDomainStore, Entity, DictInitService } from 'oo-rest-mobx';
import { observable } from 'mobx';

export class WorkPlanStore extends MobxDomainStore {
  @observable
  startedList?: Entity[];
}
export class WorkPlanService extends DomainService<WorkPlanStore> implements DictInitService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'topicWorkPlan', storeClass: WorkPlanStore, restClient });
  }
  listStarted() {
    return this.restClient.post(this.getApiUri('listStarted')).then(res => ((this.store.startedList = res), res));
  }
  initDictList() {
    this.listStarted();
  }
}
