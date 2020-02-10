import React from 'react';
import {
  EntityPageList,
  EntityColumnProps,
  DomainService,
  SimpleSearchForm,
  ListOptions,
  StringUtil,
  Consts,
} from 'oo-rest-mobx';
import { dictService, reviewPlanService } from '../../services';
import { observer } from 'mobx-react';
const columns: EntityColumnProps[] = [
  { title: '计划标题', dataIndex: 'planName' },
  { title: '立项年度', dataIndex: 'planYear' },
  { title: '评比类型', dataIndex: 'reviewTypeCode', render: dictService.dictRender.bind(null, 'res-review-type') },
  { title: '申报开始日期', dataIndex: 'planBeginDay' },
  { title: '申报截止日期', dataIndex: 'planEndDay' },
];

@observer
export class ReviewPlanList extends EntityPageList {
  get domainService(): DomainService {
    return reviewPlanService;
  }
  get columns(): EntityColumnProps[] {
    return columns;
  }
  getQueryParam(): ListOptions {
    const param = { order: [['planBeginDay', 'desc']] } as ListOptions;
    const { searchKey } = this.domainService.store.searchParam;
    if (StringUtil.isNotBlank(searchKey)) {
      param.criteria = { like: [['planName', `%${searchKey}%`]] };
    }
    return param;
  }
  get name() {
    return '评比计划';
  }
  getSearchForm() {
    return ReviewPlanSearchForm;
  }
  getOperatorVisible() {
    return Consts.AllOperator;
  }
}

export class ReviewPlanSearchForm extends SimpleSearchForm {
  placeholder = '标题';
}
