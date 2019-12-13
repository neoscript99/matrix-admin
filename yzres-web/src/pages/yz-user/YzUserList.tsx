import React from 'react';
import { UserList, AdminPageProps, UserSearchForm } from 'oo-rest-mobx';
import { YzUserForm } from './YzUserForm';

export class YzUserList extends UserList {
  constructor(props: AdminPageProps) {
    super(props);
  }

  get domainService() {
    return this.props.services.userService;
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
