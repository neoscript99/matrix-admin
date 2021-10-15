import { AbstractClient, DomainService, DomainStore, Entity, LoginInfo, RoleEntity, UserEntity } from './index';

export interface UserRoleEntity extends Entity {
  user: UserEntity;
  role: RoleEntity;
}
export class UserRoleService extends DomainService<UserRoleEntity> {
  constructor(restClient: AbstractClient) {
    super({ domain: 'userRole', storeClass: DomainStore, restClient });
  }
  get packageName() {
    return 'sys';
  }
}
