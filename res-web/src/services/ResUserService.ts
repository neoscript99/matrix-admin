import { AbstractClient, UserService } from 'oo-rest-mobx';
import { loginService } from './index';

export class ResUserService extends UserService {
  constructor(restClient: AbstractClient) {
    super(restClient, 'resUser');
  }

  isMainManager() {
    return loginService.hasRole('ResMainManager');
  }
}
