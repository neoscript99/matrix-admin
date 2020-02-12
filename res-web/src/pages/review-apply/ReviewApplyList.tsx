import React from 'react';
import { EntityPageList, EntityColumnProps } from 'oo-rest-mobx';
import { dictService, reviewPlanService } from '../../services';
import { Collapse } from 'antd';
import { PlanCard } from '../../components';

export abstract class ReviewApplyList extends EntityPageList {
  static planColumns: EntityColumnProps[] = [
    { title: '评比计划', dataIndex: 'reviewPlan.planName' },
    {
      title: '评比状态',
      dataIndex: 'reviewPlan.planStatusCode',
      render: dictService.dictRender.bind(null, 'res-plan-status'),
    },
  ];
  abstract get reviewTypeCode(): string;
  render() {
    const { startedList } = reviewPlanService.store;
    const planList = startedList?.filter(plan => plan.reviewTypeCode === this.reviewTypeCode);
    return (
      <React.Fragment>
        {super.render()}
        {planList && planList.length > 0 && (
          <Collapse style={{ marginTop: '1em' }} defaultActiveKey="1">
            <Collapse.Panel header="进行中的评比计划" key="1">
              <div className="flex-row">
                {planList.map(plan => (
                  <PlanCard key={plan.id} plan={plan} onApply={this.handleApply} />
                ))}
              </div>
            </Collapse.Panel>
          </Collapse>
        )}
      </React.Fragment>
    );
  }
  handleApply = reviewPlan => {
    const item = { reviewPlan };
    const formProps = this.genFormProps('提交', item);
    this.setState({ formProps });
  };
}
