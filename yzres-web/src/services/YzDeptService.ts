import { AbstractClient, DomainService, MobxDomainStore, DictInitService } from 'oo-rest-mobx';

export class YzDeptService extends DomainService<MobxDomainStore> implements DictInitService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'yzDept', storeClass: MobxDomainStore, restClient });
  }

  initDictList() {
    this.listAll({ orders: ['seq'] });
  }
}
