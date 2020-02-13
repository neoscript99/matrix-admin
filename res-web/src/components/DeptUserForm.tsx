import React from 'react';
import { EntityForm, EntityFormProps } from 'oo-rest-mobx';
import { resUserService } from '../services';

export interface DeptUserFormState {
  deptUserList?: any[];
}
export class DeptUserForm<
  P extends EntityFormProps = EntityFormProps,
  S extends DeptUserFormState = DeptUserFormState
> extends EntityForm<P, S> {
  async componentDidMount() {
    const deptUserList = await resUserService.getDeptUsersWithIdCard();
    this.setState({ deptUserList });
  }
}
