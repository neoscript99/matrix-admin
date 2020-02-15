import React from 'react';
import {
  EntityPageList,
  EntityColumnProps,
  ListOptions,
  StringUtil,
  TooltipLabel,
  EntityListProps,
  EntityListState,
} from 'oo-rest-mobx';
import { dictService, reviewPlanService } from '../../services';
import { Collapse } from 'antd';
import { PlanCard } from '../../components';

export abstract class ReviewApplyList<
  P extends EntityListProps = EntityListProps,
  S extends EntityListState = EntityListState
> extends EntityPageList<P, S> {
  static planColumns: EntityColumnProps[] = [
    { title: '评比计划', dataIndex: 'reviewPlan.planName' },
    {
      title: <TooltipLabel label="评比状态" tooltip="开始评分统计后的记录不能删除或修改" />,
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
  getOperatorVisible() {
    return { update: true, view: true, delete: true };
  }
  getOperatorEnable() {
    const { selectedRowKeys } = this.state;
    const selectedNum = selectedRowKeys ? selectedRowKeys.length : 0;
    const editable = selectedNum === 1 && this.getSelectItem()?.reviewPlan.planStatusCode === 'going';
    return { update: editable, view: selectedNum === 1, delete: editable };
  }

  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const { searchKey } = this.domainService.store.searchParam;
    if (StringUtil.isNotBlank(searchKey)) {
      const key = `%${searchKey}%`;
      param.criteria = {
        or: { like: [[this.columns[0].dataIndex as string, key]], reviewPlan: { like: [['planName', key]] } },
      };
    }
    return param;
  }
}
