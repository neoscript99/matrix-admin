import React from 'react';
import { EntityPageList, EntityColumnProps, SimpleSearchForm, ListOptions, StringUtil } from 'oo-rest-mobx';
import { dictService, initialApplyService } from '../../services';
import { observer } from 'mobx-react';
const columns: EntityColumnProps[] = [
  { title: '计划标题', dataIndex: 'plan.planName', width: '20em' },
  { title: '立项年度', dataIndex: 'plan.planYear' },
  { title: '课题名称', dataIndex: 'topic.topicName' },
  {
    title: '课题状态',
    dataIndex: 'topic.topicStatusCode',
    render: dictService.dictRender.bind(null, 'yz-res-topic-status'),
  },
  { title: '申请状态', dataIndex: 'statusCode', render: dictService.dictRender.bind(null, 'yz-res-apply-status') },
];

@observer
export class InitialApplyList extends EntityPageList {
  get columns() {
    return columns;
  }

  get domainService() {
    return initialApplyService;
  }

  getSearchForm() {
    return InitialApplySearchForm;
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
