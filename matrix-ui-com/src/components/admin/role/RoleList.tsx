import React from 'react';
import { AdminPageProps } from '../';
import { TableUtil } from '../../../utils';
import { EntityColumnProps, EntityList, SimpleSearchForm } from '../../layout';
import { StringUtil, DomainService, ListOptions } from 'matrix-ui-service';

const { booleanLabel, timeFormatter } = TableUtil;
const columns: EntityColumnProps[] = [
  { title: '角色名', dataIndex: 'roleName' },
  { title: '角色代码(unique)', dataIndex: 'roleCode' },
  { title: '启用', dataIndex: 'enabled', render: booleanLabel },
  { title: '可编辑', dataIndex: 'editable', render: booleanLabel },
  { title: '描述', dataIndex: 'description' },
  { title: '修改时间', dataIndex: 'lastUpdated', render: timeFormatter },
];

export class RoleList extends EntityList<AdminPageProps> {
  static defaultProps: Partial<AdminPageProps> = {
    name: '角色',
    operatorVisible: {},
  };
  constructor(props: AdminPageProps) {
    super(props);
  }

  get domainService(): DomainService {
    return this.props.services.roleService;
  }

  get columns(): EntityColumnProps[] {
    return columns;
  }
  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const { searchParam } = this.domainService.store;
    if (searchParam && StringUtil.isNotBlank(searchParam.searchKey)) {
      const key = `${searchParam.searchKey}%`;
      param.criteria = { ilike: [['roleName', key]] };
    }
    return param;
  }

  getSearchForm() {
    return RoleSearchForm;
  }
}

export class RoleSearchForm extends SimpleSearchForm {
  placeholder = '角色名';
}
