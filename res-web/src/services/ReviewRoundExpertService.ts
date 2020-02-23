import { AbstractClient, DomainService, MobxDomainStore, Entity } from 'oo-rest-mobx';

export class ReviewRoundExpertService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'reviewRoundExpert', storeClass: MobxDomainStore, restClient });
  }

  listByExpert(): Promise<Entity[]> {
    return this.postApi('listByExpert');
  }
}
