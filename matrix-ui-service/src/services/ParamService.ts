import { AbstractClient } from '../rest';
import { DomainStore } from './DomainStore';
import { DomainService } from './DomainService';
import { Entity } from './index';
export interface MatrixConfig {
  defaultRoles: string;
  casClientEnabled: boolean;
  tokenExpireMinutes: number;
  filePreviewEnable: boolean;
  filePreviewRoot: string;
  fileDownloadUrl: string;
  devLogin: boolean;
}
export interface ParamEntity extends Entity {
  //后台定义的菜单对应功能代码
  code: string;
  name: string;
  value: any;
  type: any;
}

export class ParamService extends DomainService<ParamEntity> {
  constructor(restClient: AbstractClient) {
    super({ domain: 'param', storeClass: DomainStore, restClient });
    //系统参数系统启动后就获取，不需要登录
    this.initStore();
  }

  async initStore() {
    const list = await this.list({});
    const exList = await this.listExtra();
    this.store.allList = list.results.concat(exList);
    this.fireStoreChange();
  }

  listExtra() {
    return this.postApi('listExtra');
  }

  getByCode(code: string): ParamEntity | undefined {
    const p = this.store.allList.find((param) => param.code === code);
    return p && (p as ParamEntity);
  }

  getMatrixConfig(): MatrixConfig | undefined {
    return this.getByCode('MatrixConfig')?.value;
  }
}
