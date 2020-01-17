import React from 'react';
import { EntityPageList, EntityColumnProps, SimpleSearchForm, ListOptions, StringUtil } from 'oo-rest-mobx';
import { dictService, initialPlanService } from '../../services';
import { observer } from 'mobx-react';
import { InitialPlanForm } from './InitialPlanForm';
const columns: EntityColumnProps[] = [
  { title: '计划标题', dataIndex: 'planName' },
  { title: '立项年度', dataIndex: 'planYear' },
  { title: '计划类型', dataIndex: 'planCateCode', render: dictService.dictRender.bind(null, 'res-plan-cate') },
  { title: '申报开始日期', dataIndex: 'planBeginDay' },
  { title: '申报截止日期', dataIndex: 'planEndDay' },
  { title: '限制申请数', dataIndex: 'maxNumberPerDept' },
];

@observer
export class InitialPlanList extends EntityPageList {
  render() {
    //依赖dictService.store.allList
    console.log('WorkPlanList.render: ', dictService.store.allList.length);
    return super.render();
  }

  get columns() {
    return columns;
  }

  get domainService() {
    return initialPlanService;
  }

  getEntityForm() {
    return InitialPlanForm;
  }
  getSearchForm() {
    return InitialPlanSearchForm;
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

export class InitialPlanSearchForm extends SimpleSearchForm {
  placeholder = '计划标题';
}
