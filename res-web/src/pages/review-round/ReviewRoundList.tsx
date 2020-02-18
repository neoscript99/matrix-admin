import React, { CSSProperties } from 'react';
import { EntityList, EntityColumnProps, DomainService, ListOptions, Entity, EntityListProps } from 'oo-rest-mobx';
import { dictService, reviewRoundService } from '../../services';
import { observer } from 'mobx-react';
import { ReviewRoundForm } from './ReviewRoundForm';
import { Table } from 'antd';
const columns: EntityColumnProps[] = [
  { title: '评分轮次', dataIndex: 'name' },
  { title: '开始日期', dataIndex: 'beginDay' },
  { title: '截止日期', dataIndex: 'endDay' },
];
interface P extends EntityListProps {
  plan: Entity;
}
@observer
export class ReviewRoundList extends EntityList<P> {
  constructor(props, context) {
    super(props, context);
    this.tableProps.pagination = { hideOnSinglePage: true, pageSize: 999 };
    this.tableProps.rowSelection = undefined;
  }
  render() {
    const { dataList } = this.state;
    return <Table dataSource={dataList} columns={this.columns} {...this.tableProps}></Table>;
  }
  get domainService(): DomainService {
    return reviewRoundService;
  }
  get columns(): EntityColumnProps[] {
    return columns;
  }
  getQueryParam(): ListOptions {
    const { plan } = this.props;
    return { criteria: { eq: [['plan.id', plan.id]] } };
  }
  get name() {
    return '专家评分轮次';
  }
  getEntityForm() {
    return ReviewRoundForm;
  }
}
