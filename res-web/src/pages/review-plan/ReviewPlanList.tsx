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
import { ReviewPlanForm } from './ReviewPlanForm';
import { ReviewRoundList } from '../review-round';
const columns: EntityColumnProps[] = [
  { title: '计划标题', dataIndex: 'planName' },
  { title: '立项年度', dataIndex: 'planYear' },
  { title: '评比类型', dataIndex: 'reviewTypeCode', render: dictService.dictRender.bind(null, 'res-review-type') },
  { title: '申报开始日期', dataIndex: 'planBeginDay' },
  { title: '申报截止日期', dataIndex: 'planEndDay' },
  {
    title: '评比状态',
    dataIndex: 'planStatusCode',
    render: dictService.dictRender.bind(null, 'res-plan-status'),
  },
];

@observer
export class ReviewPlanList extends EntityPageList {
  constructor(props, context) {
    super(props, context);
    this.tableProps.expandRowByClick = true;
    this.tableProps.expandedRowRender = record => <ReviewRoundList plan={record} />;
    this.tableProps.onExpand = (expanded, record) => {
      this.tableProps.expandedRowKeys = expanded ? [record.id as string] : [];
      this.forceUpdate();
    };
  }
  get domainService(): DomainService {
    return reviewPlanService;
  }
  get columns(): EntityColumnProps[] {
    return columns;
  }
  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const { searchKey } = this.domainService.store.searchParam;
    if (StringUtil.isNotBlank(searchKey)) {
      param.criteria = { like: [['planName', `%${searchKey}%`]] };
    }
    return param;
  }
  get name() {
    return '评比计划';
  }
  getEntityForm() {
    return ReviewPlanForm;
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
