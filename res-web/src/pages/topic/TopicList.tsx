import React from 'react';
import { dictService, topicService } from '../../services';
import {
  EntityColumnProps,
  EntityPageList,
  DomainService,
  SimpleSearchForm,
  ListOptions,
  StringUtil,
} from 'oo-rest-mobx';
const baseColumns: EntityColumnProps[] = [
  { title: '课题名称', dataIndex: 'topicName' },
  { title: '课题编号', dataIndex: 'topicCode' },
  { title: '负责人', dataIndex: 'personInCharge.name' },
];
const exColumns: EntityColumnProps[] = [
  {
    title: '课题状态',
    dataIndex: 'topicStatusCode',
    render: dictService.dictRender.bind(null, 'res-topic-status'),
  },
];

export class TopicList extends EntityPageList {
  getBaseColumns(): EntityColumnProps[] {
    return baseColumns;
  }

  get columns(): EntityColumnProps[] {
    return [...baseColumns, ...exColumns];
  }

  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const { searchParam } = this.domainService.store;
    if (searchParam && StringUtil.isNotBlank(searchParam.searchKey)) {
      const key = `%${searchParam.searchKey}%`;
      param.criteria = {
        or: {
          like: [
            ['topicName', key],
            ['topicCode', searchParam.searchKey],
          ],
        },
      };
    }
    return param;
  }
  get domainService(): DomainService {
    return topicService;
  }
  getSearchForm() {
    return TopicSearchForm;
  }
}

export class TopicSearchForm extends SimpleSearchForm {
  placeholder = '课题名称、编号';
}
