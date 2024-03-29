import React from 'react';
import { AdminPageProps } from '../';
import { TableUtil } from '../../../utils';
import { EntityColumnProps, EntityPageList } from '../../layout';
import { ListOptions } from 'matrix-ui-service';

const { commonColumns } = TableUtil;
const columns: EntityColumnProps[] = [
  { title: '参数代码', dataIndex: 'code' },
  { title: '参数名称', dataIndex: 'name' },
  { title: '参数类型', dataIndex: ['type', 'name'] },
  { title: '参数值', dataIndex: 'value' },
  { title: '修改人', dataIndex: ['lastUser', 'name'] },
  commonColumns.lastUpdated,
];

export class ParamList extends EntityPageList<AdminPageProps> {
  static defaultProps: Partial<AdminPageProps> = {
    name: '系统参数',
    operatorVisible: {},
  };
  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService() {
    return this.props.services.paramService;
  }

  getQueryParam(): ListOptions {
    return {
      criteria: {},
      orders: [['code', 'asc']],
    };
  }
}
