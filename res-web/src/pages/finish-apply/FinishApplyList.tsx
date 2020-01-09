import React from 'react';
import { EntityColumnProps } from 'oo-rest-mobx';
import { dictService, finishApplyService } from '../../services';
import { observer } from 'mobx-react';
import { TopicList } from '../topic';
import { FinishApplyOperator } from './FinishApplyOperator';
import { FinishApplyForm } from './FinishApplyForm';

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
      <FinishApplyOperator
        {...{ history, location, match }}
        onChange={this.refresh.bind(this)}
        onStartFinishApply={this.startFinishApply.bind(this)}
        topic={record}
      />
    );
    return [...this.getBaseColumns(), ...columns, { title: '操作', render }];
  }

  startFinishApply(topic: any) {
    this.setState({
      formProps: this.genFormProps('结题申请', topic),
    });
  }
  genFormProps(action: string, item?: any) {
    const props = super.genFormProps(action, item);
    return { ...props, modalProps: { title: `${item.topicName}${action}` } };
  }

  get domainService() {
    return finishApplyService;
  }
  getOperatorVisible() {
    return { update: true, view: true };
  }
  getApply(): any {
    const item = this.getSelectItem();
    return item && item.finishApply;
  }
  getQueryParam() {
    const param = super.getQueryParam();
    param.criteria = { ...param.criteria, ne: [['topicStatusCode', 'created']] };
    return param;
  }
  getEntityForm() {
    return FinishApplyForm;
  }
}
