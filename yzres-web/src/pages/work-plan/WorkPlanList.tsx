import React from 'react';
import { EntityPageList, EntityColumnProps, SimpleSearchForm, ListOptions, StringUtil } from 'oo-rest-mobx';
import { dictService, workPlanService } from '../../services';
import { observer } from 'mobx-react';
import { WorkPlanForm } from './WorkPlanForm';
import moment from 'moment';
const columns: EntityColumnProps[] = [
  { title: '计划标题', dataIndex: 'planName' },
  { title: '立项年度', dataIndex: 'planYear' },
  { title: '课题类别', dataIndex: 'topicCateCode', render: dictService.dictRender.bind(null, 'yz-res-topic-cate') },
  { title: '申报开始日期', dataIndex: 'planBeginDay' },
  { title: '申报截止日期', dataIndex: 'planEndDay' },
];

@observer
export class WorkPlanList extends EntityPageList {
  get columns() {
    return columns;
  }

  get domainService() {
    return workPlanService;
  }

  handleUpdate() {
    const item = this.getSelectItem();
    if (item) {
      const updateItem = {
        ...item,
        planBeginDay: item.planBeginDay && moment(item.planBeginDay),
        planEndDay: item.planEndDay && moment(item.planEndDay),
        finishDeadline: item.finishDeadline && moment(item.finishDeadline),
      };
      this.setState({
        formProps: this.getFormProps('修改', updateItem),
      });
    }
  }

  getEntityForm() {
    return WorkPlanForm;
  }
  getSearchForm() {
    return WorkPlanSearchForm;
  }
  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const { searchParam } = this.domainService.store;
    if (searchParam && StringUtil.isNotBlank(searchParam.searchKey)) {
      const key = `%${searchParam.searchKey}%`;
      param.criteria = { like: [['planName', key]] };
    }
    return param;
  }
}

export class WorkPlanSearchForm extends SimpleSearchForm {
  placeholder = '计划标题';
}
