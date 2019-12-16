import { AbstractClient, DomainService, MobxDomainStore, Entity, UserFormService } from 'oo-rest-mobx';

export class YzUserService extends DomainService<MobxDomainStore> implements UserFormService {
  constructor(restClient: AbstractClient) {
    super({ domain: 'yzUser', storeClass: MobxDomainStore, restClient });
  }
  saveUserRoles(user: Entity, roleIds: string[]) {
    return this.restClient.post(this.getApiUri('saveWithRoles'), { user, roleIds });
  }
}
