import React from 'react';
import { EntityPageList, EntityColumnProps, SimpleSearchForm, ListOptions, StringUtil } from 'oo-rest-mobx';
import { dictService, initialApplyService, workPlanService } from '../../services';
import { observer } from 'mobx-react';
import { Collapse } from 'antd';
import { WorkPlanCard } from '../work-plan';
import { InitialApplyForm } from './InitialApplyForm';
const columns: EntityColumnProps[] = [
  { title: '计划标题', dataIndex: 'plan.planName', width: '20em' },
  { title: '立项年度', dataIndex: 'plan.planYear' },
  { title: '课题名称', dataIndex: 'topic.topicName' },
  { title: '课题编号', dataIndex: 'topic.topicCode' },
  { title: '申请状态', dataIndex: 'statusCode', render: dictService.dictRender.bind(null, 'yz-res-apply-status') },
];

@observer
export class InitialApplyList extends EntityPageList {
  get columns() {
    return columns;
  }

  handleApply = plan => {
    const item = { plan };
    const formProps = this.getFormProps('提交', item);
    formProps.title = plan.planName;
    this.setState({ formProps });
  };

  getFormProps(action: string, item?: any) {
    const props = super.getFormProps(action, item);
    return { ...props, containerProps: { width: '48em' } };
  }
  render() {
    const { startedList: planList } = workPlanService.store;
    //依赖dictService.store.allList
    console.log('WorkPlanList.render: ', dictService.store.allList.length);
    return (
      <React.Fragment>
        {super.render()}
        {planList && (
          <Collapse defaultActiveKey={['1']} style={{ marginTop: '1em' }}>
            <Collapse.Panel header="进行中的立项申报计划" key="1">
              <div className="flex-row">
                {planList.map(plan => (
                  <WorkPlanCard key={plan.id} plan={plan} onApply={this.handleApply} />
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

  getSearchForm() {
    return InitialApplySearchForm;
  }

  getEntityForm() {
    return InitialApplyForm;
  }

  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const { searchParam } = this.domainService.store;
    if (searchParam && StringUtil.isNotBlank(searchParam.searchKey)) {
      const key = `%${searchParam.searchKey}%`;
      param.criteria = {
        or: { plan: { like: [['planName', key]] }, topic: { like: [['topicName', key]] } },
      };
    }
    return param;
  }
}

export class InitialApplySearchForm extends SimpleSearchForm {
  placeholder = '计划标题、课题名';
}
