import { AbstractClient, UserService, Entity, DeptEntity } from 'oo-rest-mobx';
import { loginService } from './index';

export class ResUserService extends UserService {
  constructor(restClient: AbstractClient) {
    super(restClient, 'resUser');
  }

  isMainManager() {
    return loginService.hasRole('ResMainManager');
  }

  /**
   * 成功返回 true
   * @param resUser
   */
  idCardCheck(resUser: Entity): Promise<boolean> {
    return this.postApi('idCardCheck', resUser);
  }

  async getDeptUsersWithIdCard() {
    return loginService.dept && (await this.getDeptUsers(loginService.dept)).filter(user => !!user.idCard);
  }
}
