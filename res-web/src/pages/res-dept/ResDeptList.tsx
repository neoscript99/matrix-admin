import React from 'react';
import { DeptList, AdminPageProps } from 'oo-rest-mobx';
import { ResDeptForm } from './ResDeptForm';

export class ResDeptList extends DeptList {
  constructor(props: AdminPageProps) {
    super(props);
  }
  get domainService() {
    return this.props.services.deptService;
  }

  getExtraColumns() {
    return [
      { title: '机构类型', dataIndex: 'type.name' },
      { title: '班级数', dataIndex: 'classNumber' },
      { title: '默认申报数', dataIndex: 'defaultApplyNum' },
      { title: '联系人', dataIndex: 'contact' },
      { title: '联系电话', dataIndex: 'telephone' },
    ];
  }
  getEntityForm() {
    return ResDeptForm;
  }
}
