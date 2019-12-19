import React from 'react';
import { DeptList, AdminPageProps } from 'oo-rest-mobx';
import { YzDeptForm } from './YzDeptForm';

export class YzDeptList extends DeptList {
  constructor(props: AdminPageProps) {
    super(props);
  }
  get domainService() {
    return this.props.services.deptService;
  }

  getExtraColumns() {
    return [
      { title: '联系人', dataIndex: 'contact' },
      { title: '联系电话', dataIndex: 'telephone' },
      { title: '默认申报数', dataIndex: 'defaultApplyNum' },
    ];
  }
  getEntityForm() {
    return YzDeptForm;
  }
}
