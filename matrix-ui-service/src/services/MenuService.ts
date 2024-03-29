import { Entity, LoginInfo } from './index';
import { DomainStore } from './DomainStore';
import { DomainService } from './DomainService';
import { AbstractClient } from '../rest';

export interface MenuEntity extends Entity {
  //后台定义的菜单对应功能代码
  app?: string;
  label: string;
  seq: number;
}

export interface MenuNode {
  menu: MenuEntity;
  subMenus: MenuNode[];
}

export class MenuStore extends DomainStore<MenuEntity> {
  menuTree: MenuNode = { menu: { label: 'none', seq: 0 }, subMenus: [] };
}

export class MenuService extends DomainService<MenuEntity, MenuStore> {
  constructor(restClient: AbstractClient) {
    super({ domain: 'menu', storeClass: MenuStore, restClient });
  }

  getMenuTree() {
    this.postApi('menuTree').then((data) => {
      this.store.menuTree = data;
      this.fireStoreChange();
      return data;
    });
  }

  afterLogin(loginInfo: LoginInfo) {
    super.afterLogin(loginInfo);
    this.getMenuTree();
  }
}
