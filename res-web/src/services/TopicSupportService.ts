import { AbstractClient, DomainService, MobxDomainStore, ListResult } from 'oo-rest-mobx';

export class TopicSupportService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'topicSupport', storeClass: MobxDomainStore, restClient });
  }

  getTopicSupports(topicId: string) {
    return this.list({ criteria: { eq: [['topic.id', topicId]] } }).then(res => res.results.map(v => v.support));
  }
}
