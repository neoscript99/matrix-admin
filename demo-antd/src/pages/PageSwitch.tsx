import React from 'react';
import { Route, Switch } from 'react-router';
import { Welcome } from './Welcome';
import { RoleList, DeptList, NoteList, ParamList, UserList, UserRoleList, UserProfile } from 'matrix-ui-com';
import { yard } from '../services';
const { adminServices } = yard;
export const PageSwitch = () => {
  return (
    <Switch>
      <Route path={`/Role`} render={() => <RoleList services={adminServices} />} />
      <Route path={`/UserRole`} render={() => <UserRoleList services={adminServices} />} />
      <Route path={`/User`} render={() => <UserList services={adminServices} />} />
      <Route path={`/Dept`}>{() => <DeptList services={adminServices} />}</Route>
      <Route path={`/Note`} render={() => <NoteList services={adminServices} name="通知" />} />
      <Route path={`/Param`} render={() => <ParamList services={adminServices} />} />
      <Route path={`/Profile`} render={() => <UserProfile services={adminServices} />} />
      <Route render={() => <Welcome />} />
    </Switch>
  );
};
