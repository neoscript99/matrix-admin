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
import { ReviewPlanList } from './review-plan';
import { PaperList } from './review-apply';
import { TopicAchieveList } from './review-apply';
import { ExpertReviewList } from './expert-review';

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
        <Route path={`${pathPrefix}Dept/`}>
          {() => <DeptList services={adminServices} operatorVisible={allOp} name="机构" />}
        </Route>
        <Route path={`${pathPrefix}ResDept/`}>
          {() => <ResDeptList services={adminServices} operatorVisible={allOp} name="机构" />}
        </Route>
        <Route path={`${pathPrefix}Note/`} render={() => <NoteList services={adminServices} name="通知" />} />
        <Route path={`${pathPrefix}Param/`} render={() => <ParamList services={adminServices} name="参数" />} />
        <Route path={`${pathPrefix}Profile/`} render={() => <ResUserProfile services={adminServices} />} />
        <Route path={`${pathPrefix}InitialPlan/`}>
          {() => <InitialPlanList operatorVisible={allOp} name="课题立项申报计划" />}
        </Route>
        <Route path={`${pathPrefix}InitialApply/`} component={InitialApplyList} />

        <Route path={`${pathPrefix}Topic/`}>{() => <TopicList name="课题" />}</Route>
        <Route path={`${pathPrefix}TopicMember/`}>{props => <TopicMember {...props} services={adminServices} />}</Route>
        <Route path={`${pathPrefix}FinishApply/`} component={FinishApplyList} />
        <Route path={`${pathPrefix}ReviewPlan/`} component={ReviewPlanList} />
        <Route path={`${pathPrefix}PaperReview/`} component={PaperList} />
        <Route path={`${pathPrefix}TopicReview/`} component={TopicAchieveList} />
        <Route path={`${pathPrefix}ExpertReview/`} component={ExpertReviewList} />
        <Route render={() => <Welcome />} />
      </Switch>
    );
  }
}

export default PageSwitch;
