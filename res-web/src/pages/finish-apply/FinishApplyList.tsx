import React from 'react';
import { EntityColumnProps } from 'oo-rest-mobx';
import { dictService, finishApplyService } from '../../services';
import { observer } from 'mobx-react';
import { TopicList } from '../topic';
import { FinishApplyOperator } from './FinishApplyOperator';
import { Button } from 'antd';

const statusRender = (code: string) => {
  const label = dictService.dictRender('res-apply-status', code);
  return label || '未发起';
};
const columns: EntityColumnProps[] = [
  {
    title: '立项申请状态',
    dataIndex: 'initialApply.statusCode',
    render: statusRender,
  },
  {
    title: '结题申请状态',
    dataIndex: 'finishApply.statusCode',
    render: statusRender,
  },
];

@observer
export class FinishApplyList extends TopicList {
  get columns(): EntityColumnProps[] {
    const { history, location, match } = this.props;
    const render = (text, record) => (
      <FinishApplyOperator {...{ history, location, match }} onChange={this.refresh.bind(this)} topic={record} />
    );
    return [...this.getBaseColumns(), ...columns, { title: '操作', render }];
  }

  get domainService() {
    return finishApplyService;
  }
  getOperatorVisible() {
    return { view: true };
  }

  getQueryParam() {
    const param = super.getQueryParam();
    param.criteria = { ...param.criteria, ne: [['topicStatusCode', 'created']] };
    return param;
  }
}
