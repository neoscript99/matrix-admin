import React from 'react';
import { EntityList, EntityColumnProps, TableUtil, ListOptions } from 'oo-rest-mobx';
import { achieveRoundResultService } from '../../services';
import { Button, Tag } from 'antd';

const columns: EntityColumnProps[] = [
  TableUtil.commonColumns.index,
  { title: '标题', dataIndex: 'achieve.name' },
  { title: '负责人', dataIndex: 'achieve.personInCharge.name' },
  { title: '单位', dataIndex: 'achieve.dept.name' },
  { title: '得分', dataIndex: 'average' },
  {
    title: '打分明细',
    dataIndex: 'scoresJson',
    render: value => {
      const scores: any[] = JSON.parse(value);
      return (
        <div className="flex" style={{ width: '15em', margin: -10 }}>
          {scores.map(s => (
            <Tag key={s.name}>
              {s.name}: {s.score}
            </Tag>
          ))}
        </div>
      );
    },
  },
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
  }
  render() {
    return (
      <React.Fragment>
        <Button onClick={this.goBack} icon="rollback" />
        {super.render()}
      </React.Fragment>
    );
  }
  export = () => {
    const tab = document.querySelector('table');
    if (tab) tab.id = 'datatable';
    console.log(tab);
  };
  goBack = () => {
    const { history } = this.props;
    history?.goBack();
  };

  getQueryParam(): ListOptions {
    const { match } = this.props;
    const roundId = (match?.params as any).roundId;
    if (roundId) return { criteria: { eq: [['round.id', roundId]] }, orders: [['average', 'desc']] };
    else throw '请传入正确的评比轮次';
  }

  get columns() {
    return columns;
  }

  get domainService() {
    return achieveRoundResultService;
  }
}
