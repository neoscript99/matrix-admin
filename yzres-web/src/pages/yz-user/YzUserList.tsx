import React from 'react';
import moment from 'moment';
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

  handleUpdate() {
    const item = this.getSelectItem();
    if (item)
      this.setState({
        formProps: this.getFormProps('修改', { ...item, birthDay: moment(item.birthDay) }),
      });
  }

  getExtraColumns() {
    return [
      { title: '职务职称', dataIndex: 'title' },
      { title: '联系电话', dataIndex: 'phoneNumber' },
    ];
  }
  getEntityForm() {
    return YzUserForm;
  }
  getSearchForm() {
    return UserSearchForm;
  }
}
