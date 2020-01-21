import React from 'react';
import { EntityColumnProps, EntityFormProps } from 'oo-rest-mobx';
import { dictService, initialApplyService, initialPlanService } from '../../services';
import { observer } from 'mobx-react';
import { Collapse } from 'antd';
import { InitialPlanCard } from '../initial-plan';
import { InitialApplyForm } from './InitialApplyForm';
import { InitialApplyOperator } from './InitialApplyOperator';
import { TopicList } from '../topic';

const columns: EntityColumnProps[] = [
  { title: '所属计划', dataIndex: 'initialPlan.planName' },
  {
    title: '立项申请状态',
    dataIndex: 'initialApply.statusCode',
    render: dictService.dictRender.bind(null, 'res-apply-status'),
  },
];

@observer
export class InitialApplyList extends TopicList {
  constructor(props) {
    super(props);
    this.tableProps.pagination.pageSize = 6;
  }

  get columns() {
    const { history, location, match } = this.props;
    const render = (text, record) => (
      <InitialApplyOperator {...{ history, location, match }} onChange={this.refresh.bind(this)} topic={record} />
    );
    return [...this.getBaseColumns(), ...columns, { title: '操作', render }];
  }

  handleApply = initialPlan => {
    const item = { initialPlan, prepareFinishDay: initialPlan.finishDeadline };
    const formProps = this.genFormProps('提交', item);
    this.setState({ formProps });
  };

  genFormProps(action: string, item?: any, exProps?: Partial<EntityFormProps>): EntityFormProps {
    const props = super.genFormProps(action, item, exProps);
    return { ...props, modalProps: { width: '48em', title: item.initialPlan.planName } };
  }

  render() {
    const { startedList: planList } = initialPlanService.store;
    return (
      <React.Fragment>
        {super.render()}
        {planList && (
          <Collapse style={{ marginTop: '1em' }} defaultActiveKey="1">
            <Collapse.Panel header="进行中的立项申报计划" key="1">
              <div className="flex-row">
                {planList.map(plan => (
                  <InitialPlanCard key={plan.id} plan={plan} onApply={this.handleApply} />
                ))}
              </div>
            </Collapse.Panel>
          </Collapse>
        )}
      </React.Fragment>
    );
  }

  get domainService() {
    return initialApplyService;
  }
  getEntityForm() {
    return InitialApplyForm;
  }
  getOperatorVisible() {
    return { update: true, delete: true, view: true };
  }
  getApply(): any {
    const item = this.getSelectItem();
    return item && item.initialApply;
  }

  get name() {
    return '立项课题';
  }
}
