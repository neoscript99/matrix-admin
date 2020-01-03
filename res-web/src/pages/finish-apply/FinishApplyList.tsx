import React from 'react';
import { EntityPageList, EntityColumnProps, SimpleSearchForm, ListOptions, StringUtil } from 'oo-rest-mobx';
import { dictService, topicService } from '../../services';
import { observer } from 'mobx-react';

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
export class FinishApplyList extends EntityPageList {
  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService() {
    return topicService;
  }

  getSearchForm() {
    return FinishApplySearchForm;
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
  getOperatorVisible() {
    return { update: true, delete: true, view: true };
  }
}

export class FinishApplySearchForm extends SimpleSearchForm {
  placeholder = '计划标题、课题名';
}
