import React from 'react';
import { EntityColumnProps, EntityFormProps } from 'oo-rest-mobx';
import { dictService, initialPlanService } from '../../services';
import { observer } from 'mobx-react';
import { Collapse } from 'antd';
import { InitialPlanCard } from '../initial-plan';
import { InitialApplyForm } from './InitialApplyForm';
import { InitialApplyOperate } from './InitialApplyOperate';
import { checkEditable } from './index';
import { TopicList } from '../topic';

const columns: EntityColumnProps[] = [
  { title: '所属计划', dataIndex: 'initialPlan.planName' },
  {
    title: '申请状态',
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
      <InitialApplyOperate {...{ history, location, match }} onChange={this.refresh.bind(this)} item={record} />
    );
    return [...this.getBaseColumns(), ...columns, { title: '操作', render }];
  }

  handleApply = initialPlan => {
    const item = { initialPlan };
    const formProps = this.genFormProps('提交', item);
    this.setState({ formProps });
  };

  genFormProps(action: string, item?: any, exProps?: Partial<EntityFormProps>): EntityFormProps {
    const props = super.genFormProps(action, item, exProps);
    return { ...props, modalProps: { width: '48em', title: item.initialPlan.planName } };
  }

  render() {
    const { startedList: planList } = initialPlanService.store;
    //依赖dictService.store.allList
    console.log('WorkPlanList.render: ', dictService.store.allList.length);
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

  getEntityForm() {
    return InitialApplyForm;
  }
  getOperatorVisible() {
    return { update: true, delete: true, view: true };
  }
  getOperatorEnable() {
    const value = super.getOperatorEnable();
    const item = this.getSelectItem();
    const editable = !!item && checkEditable(item.statusCode);
    return { ...value, update: value.update && editable, delete: value.delete && editable };
  }
}
