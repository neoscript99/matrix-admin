import React from 'react';
import { EntityPageList } from 'oo-rest-mobx';
import { reviewPlanService } from '../../services';
import { Collapse } from 'antd';
import { PlanCard } from '../../components';

export abstract class ReviewApplyList extends EntityPageList {
  abstract get reviewTypeCode(): string;
  render() {
    const { startedList } = reviewPlanService.store;
    const planList = startedList?.filter(plan => plan.reviewTypeCode === this.reviewTypeCode);
    return (
      <React.Fragment>
        {super.render()}
        {planList && (
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
