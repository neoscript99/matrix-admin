import { AbstractClient } from '../rest';
import { DomainStore } from './DomainStore';
import { DomainService } from './DomainService';
import { Entity, LoginInfo } from './index';

export interface DeptEntity extends Entity {
  name: string;
  seq: number;
  enabled: boolean;
}

export class DeptStore extends DomainStore<DeptEntity> {
  completeList: Entity[] = [];
  enabledList: Entity[] = [];
}

export class DeptService extends DomainService<DeptEntity, DeptStore> {
  constructor(restClient: AbstractClient, domain = 'department') {
    super({ domain, storeClass: DeptStore, restClient });
  }
  get packageName() {
    return 'sys';
  }
  afterLogin(loginInfo: LoginInfo) {
    super.afterLogin(loginInfo);
    if (this.readAuthorize(loginInfo.authorities))
      this.list({ orders: [['seq', 'asc']] }).then((res) => {
        this.store.completeList = res.results;
        this.store.enabledList = res.results.filter((dept) => dept.enabled);
        this.fireStoreChange();
      });
  }
}
