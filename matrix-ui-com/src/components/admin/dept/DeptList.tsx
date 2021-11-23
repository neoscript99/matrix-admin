import React from 'react';
import { AdminPageProps } from '../';
import { TableUtil } from '../../../utils';
import { EntityList, EntityColumnProps, SimpleSearchForm } from '../../layout';
import { ListOptions, StringUtil } from 'matrix-ui-service';
import { DeptForm } from './DeptForm';

const { commonColumns } = TableUtil;
export class DeptList extends EntityList<AdminPageProps> {
  static defaultProps = { name: '机构' };
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
      commonColumns.lastUpdated,
    ];
  }

  getExtraColumns(): EntityColumnProps[] {
    return [];
  }
  getEntityForm() {
    return DeptForm;
  }
  getSearchForm() {
    return DeptSearchForm;
  }
  getQueryParam(): ListOptions {
    const param: ListOptions = {
      orders: [['seq', 'asc']],
    };
    const { searchParam } = this.domainService.store;
    if (searchParam && StringUtil.isNotBlank(searchParam.searchKey)) {
      const key = `%${searchParam.searchKey}%`;
      param.criteria = { like: [['name', key]] };
    }
    return param;
  }
}

export class DeptSearchForm extends SimpleSearchForm {
  placeholder = '机构名(*..*)';
}
