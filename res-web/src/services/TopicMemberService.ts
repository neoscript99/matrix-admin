import { AbstractClient, DomainService, MobxDomainStore } from 'oo-rest-mobx';

export class TopicMemberService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'topicMember', storeClass: MobxDomainStore, restClient });
  }
  saveMembers(topicId: string, memberIds: string[]): Promise<number> {
    return this.postApi('saveMembers', { topicId, memberIds });
  }
  getMembers(topicId: string) {
    return this.listAll({ criteria: { eq: [['topic.id', topicId]] } }).then(res => res.results.map(tm => tm.member));
  }
}
