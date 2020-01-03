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
import { ResUserList, ResUserProfile } from './res-user';
import { ResDeptList } from './res-dept';
import { InitialPlanList } from './initial-plan';
import { InitialApplyList } from './initial-apply';
import { TopicList, TopicMember } from './topic';
import { FinishApplyList } from './finish-apply';

const allOp: OperatorSwitch = { create: true, update: true, delete: true, view: true };
export class PageSwitch extends Component<PageSwitchProps> {
  render() {
    const { pathPrefix } = this.props;
    return (
      <Switch>
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
          path={`${pathPrefix}ResUser/`}
          render={() => <ResUserList services={adminServices} operatorVisible={allOp} name="用户" />}
        />
        <Route
          path={`${pathPrefix}Dept/`}
          render={() => (
            <DeptList services={adminServices} operatorVisible={{ update: true, create: true }} name="机构" />
          )}
        />
        <Route
          path={`${pathPrefix}ResDept/`}
          render={() => (
            <ResDeptList services={adminServices} operatorVisible={{ update: true, create: true }} name="机构" />
          )}
        />
        <Route path={`${pathPrefix}Note/`} render={() => <NoteList services={adminServices} name="通知" />} />
        <Route path={`${pathPrefix}Param/`} render={() => <ParamList services={adminServices} name="参数" />} />
        <Route path={`${pathPrefix}Profile/`} render={() => <ResUserProfile services={adminServices} />} />
        <Route path={`${pathPrefix}InitialPlan/`}>
          {() => <InitialPlanList operatorVisible={allOp} name="课题立项申报计划" />}
        </Route>
        <Route path={`${pathPrefix}InitialApply/`} render={props => <InitialApplyList {...props} name="立项申报" />} />

        <Route path={`${pathPrefix}Topic/`} render={() => <TopicList name="课题" />} />
        <Route
          path={`${pathPrefix}TopicMember/`}
          render={props => <TopicMember {...props} services={adminServices} />}
        />
        <Route path={`${pathPrefix}FinishApply/`} render={() => <FinishApplyList name="结题流程" />} />
        <Route render={() => <Welcome />} />
      </Switch>
    );
  }
}

export default PageSwitch;
