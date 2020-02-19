import React from 'react';
import {
  EntityPageList,
  EntityColumnProps,
  DomainService,
  SimpleSearchForm,
  ListOptions,
  StringUtil,
  Consts,
  EntityListState,
  EntityListProps,
} from 'oo-rest-mobx';
import { dictService, reviewPlanService } from '../../services';
import { observer } from 'mobx-react';
import { ReviewPlanForm } from './ReviewPlanForm';
import { ReviewRoundList } from '../review-round';
import { Button } from 'antd';
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
interface S extends EntityListState {
  showRoundForm?: boolean;
  expandedRowKeys?: string[];
}
@observer
export class ReviewPlanList extends EntityPageList<EntityListProps, S> {
  constructor(props, context) {
    super(props, context);
    this.state.expandedRowKeys = [];
    this.tableProps.expandRowByClick = true;
    this.tableProps.expandedRowRender = record => (
      <ReviewRoundList plan={record} showForm={this.state.showRoundForm} onFormClose={this.handleRoundFormClose} />
    );
    this.tableProps.onExpand = (expanded, record) => {
      const expandedRowKeys = expanded ? [record.id as string] : [];
      this.setState({ expandedRowKeys });
    };
  }
  handleRoundFormClose = () => this.setState({ showRoundForm: false });
  handleRoundCreate(plan, e) {
    e.stopPropagation();
    this.setState({ expandedRowKeys: [plan.id as string], showRoundForm: true });
  }
  render() {
    this.tableProps.expandedRowKeys = this.state.expandedRowKeys;
    return super.render();
  }
  get domainService(): DomainService {
    return reviewPlanService;
  }
  get columns(): EntityColumnProps[] {
    return [
      ...columns,
      {
        title: '操作',
        render: (text, item) => (
          <Button size="small" type="primary" onClick={this.handleRoundCreate.bind(this, item)}>
            新增专家评分轮次
          </Button>
        ),
      },
    ];
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
    return Consts.allOperator;
  }
}

export class ReviewPlanSearchForm extends SimpleSearchForm {
  placeholder = '标题';
}
