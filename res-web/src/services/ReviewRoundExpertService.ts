import { AbstractClient, DomainService, MobxDomainStore, Entity } from 'oo-rest-mobx';

export class ReviewRoundExpertService extends DomainService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'reviewRoundExpert', storeClass: MobxDomainStore, restClient });
  }

  /**
   * 保存到allList中，注意冲突
   */
  listByExpert(): Promise<Entity[]> {
    return this.postApi('listByExpert').then((res: any[]) => {
      this.store.allList = res;
      if (res.length > 0) this.store.currentItem = res[0];
      return res;
    });
  }
}
