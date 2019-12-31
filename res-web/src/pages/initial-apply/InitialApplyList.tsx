import React from 'react';
import {
  EntityPageList,
  EntityColumnProps,
  SimpleSearchForm,
  ListOptions,
  StringUtil,
  EntityFormProps,
} from 'oo-rest-mobx';
import { dictService, initialApplyService, workPlanService } from '../../services';
import { observer } from 'mobx-react';
import { Collapse } from 'antd';
import { WorkPlanCard } from '../work-plan';
import { InitialApplyForm } from './InitialApplyForm';
import { InitialApplyOperate } from './InitialApplyOperate';

const columns: EntityColumnProps[] = [
  { title: '所属计划', dataIndex: 'plan.planName' },
  { title: '课题名称', dataIndex: 'topic.topicName' },
  { title: '课题编号', dataIndex: 'topic.topicCode' },
  { title: '负责人', dataIndex: 'topic.personInCharge.name' },
  {
    title: '申请状态',
    dataIndex: 'statusCode',
    render: dictService.dictRender.bind(null, 'res-apply-status'),
  },
];

@observer
export class InitialApplyList extends EntityPageList {
  constructor(props) {
    super(props);
    this.tableProps.pagination.pageSize = 6;
  }

  get columns() {
    const { history, location, match } = this.props;
    const render = (text, record) => (
      <InitialApplyOperate {...{ history, location, match }} onChange={this.refresh.bind(this)} item={record} />
    );
    return [...columns, { title: '操作', render }];
  }

  handleApply = plan => {
    const item = { plan };
    const formProps = this.getFormProps('提交', item);
    this.setState({ formProps });
  };

  getFormProps(action: string, item?: any): EntityFormProps {
    const props = super.getFormProps(action, item);
    return { ...props, modalProps: { width: '48em', title: item.plan.planName } };
  }

  render() {
    const { startedList: planList } = workPlanService.store;
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
