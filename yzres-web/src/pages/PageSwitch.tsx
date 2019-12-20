import React, { Component } from 'react';
import { Route, Switch } from 'react-router';
import { adminServices } from '../services';
import { Welcome } from './Welcome';
import {
  RoleList,
  DeptList,
  NoteList,
  ParamList,
  UserList,
  OperatorSwitch,
  UserRoleList,
  PageSwitchProps,
} from 'oo-rest-mobx';
import { YzUserList, YzUserProfile } from './yz-user';
import { YzDeptList } from './yz-dept';
import { WorkPlanList } from './work-plan';
import { InitialApplyList } from './initial-apply';

const allOp: OperatorSwitch = { create: true, update: true, delete: true };
export class PageSwitch extends Component<PageSwitchProps> {
  render() {
    const { pathPrefix } = this.props;
    return (
      <Switch>
        <Route
          path={`${pathPrefix}WorkPlan/`}
          render={() => <WorkPlanList operatorVisible={allOp} name="课题立项申报计划" />}
        />
        <Route path={`${pathPrefix}InitialApply/`} render={() => <InitialApplyList name="立项申报" />} />
        <Route path={`${pathPrefix}Role/`} render={() => <RoleList services={adminServices} name="角色" />} />
        <Route
          path={`${pathPrefix}UserRole/`}
          render={() => <UserRoleList services={adminServices} name="用户角色" />}
        />
        <Route
          path={`${pathPrefix}User/`}
          render={() => <UserList services={adminServices} operatorVisible={allOp} name="用户" />}
        />
        <Route
          path={`${pathPrefix}YzUser/`}
          render={() => <YzUserList services={adminServices} operatorVisible={allOp} name="用户" />}
        />
        <Route
          path={`${pathPrefix}Dept/`}
          render={() => (
            <DeptList services={adminServices} operatorVisible={{ update: true, create: true }} name="机构" />
          )}
        />
        <Route
          path={`${pathPrefix}YzDept/`}
          render={() => (
            <YzDeptList services={adminServices} operatorVisible={{ update: true, create: true }} name="机构" />
          )}
        />
        <Route path={`${pathPrefix}Note/`} render={() => <NoteList services={adminServices} name="通知" />} />
        <Route path={`${pathPrefix}Param/`} render={() => <ParamList services={adminServices} name="参数" />} />
        <Route path={`${pathPrefix}Profile/`} render={() => <YzUserProfile services={adminServices} />} />
        <Route render={() => <Welcome />} />
      </Switch>
    );
  }
}

export default PageSwitch;
