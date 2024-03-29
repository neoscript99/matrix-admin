import { DomainStore } from './DomainStore';
import { AbstractClient, DeptEntity, DomainService, Entity } from './index';

export interface UserEntity extends Entity {
  account: string;
  name?: string;
  dept: DeptEntity;
}

export class UserService extends DomainService<UserEntity> {
  deptUserMap: Record<string, UserEntity[]> = {};
  constructor(restClient: AbstractClient, domain = 'user') {
    super({ domain, storeClass: DomainStore, restClient });
  }
  async getDeptUsers(dept: DeptEntity) {
    const id = dept.id as string;
    if (!this.deptUserMap[id])
      this.deptUserMap[id] = (
        await this.list({
          criteria: {
            eq: [
              ['dept.id', id],
              ['enabled', true],
            ],
          },
          orders: [['name', 'asc']],
        })
      ).results as UserEntity[];

    return this.deptUserMap[id];
  }
  //部门用户更新、删除后进行刷新
  clearDeptUsers(dept: DeptEntity) {
    const id = dept.id as string;
    if (this.deptUserMap[id]) delete this.deptUserMap[id];
  }
  saveUserRoles(user: Entity, roleIds: string[]) {
    return this.postApi('saveWithRoles', { user, roleIds });
  }
  resetPassword(user: Entity) {
    return this.postApi('resetPassword', { userId: user.id });
  }
}
