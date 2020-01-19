import React from 'react';
import { UserList, AdminPageProps, UserSearchForm, EntityListState } from 'oo-rest-mobx';
import { ResUserForm } from './ResUserForm';
import { resUserService } from '../../services';

export class ResUserList<
  P extends AdminPageProps = AdminPageProps,
  S extends EntityListState = EntityListState
> extends UserList<P, S> {
  get domainService() {
    return resUserService;
  }
  getExtraColumns() {
    return [
      { title: '职务职称', dataIndex: 'title' },
      { title: '联系电话', dataIndex: 'phoneNumber' },
    ];
  }
  getEntityForm() {
    return ResUserForm;
  }
  getSearchForm() {
    return UserSearchForm;
  }
}
