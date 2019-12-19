import React from 'react';
import { EntityPageList, EntityColumnProps } from 'oo-rest-mobx';
import { dictService, workPlanService } from '../../services';
const columns: EntityColumnProps[] = [
  { title: '计划标题', dataIndex: 'planName' },
  { title: '立项年度', dataIndex: 'planYear' },
  { title: '课题类别', dataIndex: 'topicCateId', render: dictService.dictRender.bind(null, 'yz-res-topic-cate') },
  { title: '申报开始日期', dataIndex: 'planBeginDay' },
  { title: '申报截止日期', dataIndex: 'planEndDay' },
];
export class WorkPlanList extends EntityPageList {
  get columns() {
    return columns;
  }

  get domainService() {
    return workPlanService;
  }
}
