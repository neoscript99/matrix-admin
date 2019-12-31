import { AbstractClient, DeptService } from 'oo-rest-mobx';

export class ResDeptService extends DeptService {
  constructor(restClient: AbstractClient) {
    super(restClient, 'resDept');
  }
}
