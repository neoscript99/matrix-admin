import React from 'react';
import { dictService, topicService } from '../../services';
import { EntityColumnProps, EntityPageList, DomainService } from 'oo-rest-mobx';
const columns: EntityColumnProps[] = [
  { title: '课题名称', dataIndex: 'topicName' },
  {
    title: '课题状态',
    dataIndex: 'topic.topicStatusCode',
    render: dictService.dictRender.bind(null, 'yz-res-topic-status'),
  },
];

export class TopicList extends EntityPageList {
  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService(): DomainService {
    return topicService;
  }
}
