import { AbstractClient, DomainService, MobxDomainStore } from 'oo-rest-mobx';

export class TopicMemberService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'topicMember', storeClass: MobxDomainStore, restClient });
  }
  saveMembers(topicId: string, memberIds: string[]): Promise<number> {
    return this.restClient.post(this.getApiUri('saveMembers'), { topicId, memberIds });
  }
}
