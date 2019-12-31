import React from 'react';
import moment from 'moment';
import { UserList, AdminPageProps, UserSearchForm, UserListProps, EntityListState } from 'oo-rest-mobx';
import { ResUserForm } from './ResUserForm';
import { resUserService } from '../../services';

export class ResUserList<
  P extends UserListProps = UserListProps,
  S extends EntityListState = EntityListState
> extends UserList<P, S> {
  get domainService() {
    return resUserService;
  }

  handleUpdate() {
    const item = this.getSelectItem();
    if (item)
      this.setState({
        formProps: this.getFormProps('修改', { ...item, birthDay: item.birthDay && moment(item.birthDay) }),
      });
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
