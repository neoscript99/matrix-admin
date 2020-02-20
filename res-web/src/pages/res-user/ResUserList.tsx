import React from 'react';
import { UserList, AdminPageProps, EntityListState, SearchFormProps, UserSearchForm } from 'oo-rest-mobx';
import { ResUserForm } from './ResUserForm';
import { deptService, resUserService } from '../../services';

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
  searchFormRender(props: SearchFormProps): React.ReactNode {
    const deptList = resUserService.isMainManager() ? deptService.store.enabledList : undefined;
    return <UserSearchForm {...props} deptList={deptList} />;
  }
  handleFormSuccess(item: any): void {
    //用户新增或修改后，清除部门用户缓存
    resUserService.clearCurrentDeptUsers();
    super.handleFormSuccess(item);
  }
  handleDelete(): Promise<any> {
    return super.handleDelete().then(res => {
      //用户删除后，清除部门用户缓存
      resUserService.clearCurrentDeptUsers();
      return res;
    });
  }
}
