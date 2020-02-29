import React from 'react';
import { EntityList, EntityColumnProps, TableUtil, ListOptions, ReactUtil } from 'oo-rest-mobx';
import { achieveRoundResultService } from '../../services';
import { Button } from 'antd';

const columns: EntityColumnProps[] = [
  TableUtil.commonColumns.index,
  { title: '标题', dataIndex: 'achieve.name' },
  { title: '负责人', dataIndex: 'achieve.personInCharge.name' },
  { title: '单位', dataIndex: 'achieve.dept.name' },
  { title: '正文', dataIndex: 'achieve.paperFile.name' },
  { title: '得分', dataIndex: 'average' },
  { title: '打分明细', dataIndex: 'expertScores', render: ReactUtil.wordBreakTextRender.bind(undefined, 10) },
  { title: '备注', dataIndex: 'message' },
];

export class ReviewResultList extends EntityList {
  constructor(a, b) {
    super(a, b);
    this.tableProps.pagination = { hideOnSinglePage: true, pageSize: 999 };
    this.tableProps.rowSelection = undefined;
  }
  componentDidMount(): void {
    this.query();
    const { match } = this.props;
    console.log(match);
  }
  render() {
    return (
      <React.Fragment>
        <Button onClick={this.goBack} icon="rollback" />
        {super.render()}
      </React.Fragment>
    );
  }
  goBack = () => {
    const { history } = this.props;
    history?.goBack();
  };
  getQueryParam(): ListOptions {
    const param: ListOptions = { orders: [['average', 'desc']] };
    return param;
  }

  get columns() {
    return columns;
  }

  get domainService() {
    return achieveRoundResultService;
  }
}
