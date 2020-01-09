import { AbstractClient, DomainService, MobxDomainStore } from 'oo-rest-mobx';

export class TopicService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'topic', storeClass: MobxDomainStore, restClient });
  }

  checkQualification(initialPlanId, deptId) {
    return this.postApi('checkQualification', { initialPlanId, deptId });
  }
}
