import { AbstractClient, DomainService, MobxDomainStore, Entity } from 'oo-rest-mobx';

export class ReviewRoundService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'reviewRound', storeClass: MobxDomainStore, restClient });
  }

  runResult({ id }: Entity): Promise<Entity> {
    return this.postApi('runResult', { id });
  }
}
