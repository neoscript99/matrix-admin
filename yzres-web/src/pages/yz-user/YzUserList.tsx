import React from 'react';
import { UserList, AdminPageProps, UserSearchForm } from 'oo-rest-mobx';
import { YzUserForm } from './YzUserForm';
import { yzUserService } from '../../services';

export class YzUserList extends UserList {
  constructor(props: AdminPageProps) {
    super(props);
  }

  get domainService() {
    return yzUserService;
  }

  getUserColumns() {
    return super.getUserColumns();
  }
  getEntityForm() {
    return YzUserForm;
  }
  getSearchForm() {
    return UserSearchForm;
  }
}
