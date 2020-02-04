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
      { title: '手机号码', dataIndex: 'cellPhone' },
    ];
  }
  getEntityForm() {
    return ResUserForm;
  }
  getSearchForm() {
    return UserSearchForm;
  }
}
