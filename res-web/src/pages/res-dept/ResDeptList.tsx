import React from 'react';
import { DeptList, AdminPageProps, commonColumns } from 'oo-rest-mobx';
import { ResDeptForm } from './ResDeptForm';
import { message } from 'antd';

export class ResDeptList extends DeptList {
  constructor(props: AdminPageProps) {
    super(props);
  }
  get domainService() {
    return this.props.services.deptService;
  }

  get columns() {
    return [
      { title: '序号', dataIndex: 'seq' },
      { title: '机构名', dataIndex: 'name' },
      ...this.getExtraColumns(),
      commonColumns.enabled,
    ];
  }
  getExtraColumns() {
    return [
      { title: '机构类型', dataIndex: 'type.name' },
      { title: '班级数', dataIndex: 'classNumber' },
      { title: '限制申报数', dataIndex: 'maxApplyNum' },
      { title: '联系人', dataIndex: 'contact' },
      { title: '联系电话', dataIndex: 'telephone' },
    ];
  }
  getEntityForm() {
    return ResDeptForm;
  }

  handleDelete() {
    return super.handleDelete().catch((err: string) => {
      if (err.includes('ConstraintViolationException'))
        message.error('当前选择的机构存在关联信息（用户、课题），无法删除');
      else message.error(err);
    });
  }
}
