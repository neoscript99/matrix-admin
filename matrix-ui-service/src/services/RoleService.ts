import { AbstractClient } from '../rest';
import { DomainStore } from './DomainStore';
import { DomainService, Entity, LoginInfo } from './index';

export interface RoleEntity extends Entity {
  roleName: string;
  roleCode: string;
  description: string;
  enabled: boolean;
  editable: boolean;
  lastUpdated: Date;
}

export class RoleService extends DomainService<RoleEntity> {
  constructor(restClient: AbstractClient) {
    super({ domain: 'role', storeClass: DomainStore, restClient });
  }
  afterLogin(loginInfo: LoginInfo) {
    super.afterLogin(loginInfo);
    this.readAuthorize(loginInfo.authorities) && this.listAll({ orders: [['lastUpdated', 'desc']] });
  }
  get packageName() {
    return 'sys';
  }
}
