import React from 'react';
import { dictService, topicService, topicSupportService } from '../../services';
import {
  EntityColumnProps,
  EntityPageList,
  DomainService,
  SimpleSearchForm,
  ListOptions,
  StringUtil,
  EntityFormProps,
  Entity,
} from 'oo-rest-mobx';
import { ApplyUtil } from '../../utils/ApplyUtil';
const baseColumns: EntityColumnProps[] = [
  { title: '课题名称', dataIndex: 'topicName' },
  { title: '立项编号', dataIndex: 'initialCode' },
  { title: '单位', dataIndex: 'dept.name' },
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

  render(): JSX.Element {
    //依赖dictService.store.allList
    console.log('WorkPlanList.render: ', dictService.store.allList.length);
    return super.render();
  }

  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const {
      searchParam: { searchKey },
    } = this.domainService.store;
    if (StringUtil.isNotBlank(searchKey)) {
      param.criteria = {
        or: {
          like: [
            ['topicName', `%${searchKey}%`],
            ['initialCode', `${searchKey}%`],
          ],
        },
      };
    }
    return param;
  }

  async handleView() {
    const item = this.getSelectItem();
    if (item) {
      item.supports = await topicSupportService.getTopicSupports(item.id!);
      const formProps = this.genFormProps('查看', item, { readonly: true });
      this.setState({ formProps });
    }
  }

  get domainService(): DomainService {
    return topicService;
  }
  getSearchForm() {
    return TopicSearchForm;
  }
  getOperatorEnable() {
    const value = super.getOperatorEnable();
    const item = this.getSelectItem();
    const editable = !!item && ApplyUtil.checkEditable(this.getApply());
    return { ...value, update: value.update && editable, delete: value.delete && editable };
  }
  //当前页面要维护的申请信息，
  //立项流程的申请对应：initialApply
  //结题流程的申请对应：finishApply
  getApply(): any {
    return null;
  }
}

export class TopicSearchForm extends SimpleSearchForm {
  placeholder = '课题名称、立项编号';
}
