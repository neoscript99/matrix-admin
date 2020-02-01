import React from 'react';
import { dictService, topicMemberService, topicService, topicSupportService } from '../../services';
import {
  EntityColumnProps,
  EntityPageList,
  DomainService,
  ListOptions,
  StringUtil,
  EntityFormProps,
  Criteria,
} from 'oo-rest-mobx';
import { ApplyUtil } from '../../utils/ApplyUtil';
import { Button } from 'antd';
import { TopicView } from './TopicView';
import { TopicSearchForm } from './TopicSearchForm';

export class TopicList extends EntityPageList {
  getBaseColumns(): EntityColumnProps[] {
    return [
      {
        title: '课题名称',
        dataIndex: 'topicName',
        render: (text, topic) => (
          <Button type="link" onClick={this.showTopic.bind(this, topic)}>
            {text}
          </Button>
        ),
      },
      { title: '立项编号', dataIndex: 'initialCode' },
      { title: '单位', dataIndex: 'dept.name' },
      { title: '负责人', dataIndex: 'personInCharge.name' },
    ];
  }

  get columns(): EntityColumnProps[] {
    return [
      ...this.getBaseColumns(),
      {
        title: '课题状态',
        dataIndex: 'topicStatusCode',
        render: dictService.dictRender.bind(null, 'res-topic-status'),
      },
    ];
  }

  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const {
      searchParam: { topicName, initialCode, planYear, deptName },
    } = this.domainService.store;
    const criteria: Criteria = { like: [] };
    if (StringUtil.isNotBlank(topicName)) criteria.like!.push(['topicName', `%${topicName}%`]);
    if (StringUtil.isNotBlank(initialCode)) criteria.like!.push(['initialCode', `${initialCode}%`]);
    if (StringUtil.isNotBlank(deptName)) criteria.dept = { like: [['name', `%${deptName}%`]] };
    if (planYear) criteria.initialPlan = { eq: [['planYear', planYear]] };
    return { ...param, criteria };
  }

  handleView() {
    const item = this.getSelectItem();
    this.showTopic(item);
  }

  async showTopic(topic) {
    if (topic && topic.id) {
      topic.supports = await topicSupportService.getSupports(topic.id!);
      topic.members = await topicMemberService.getMembers(topic.id!);
      const formProps = {
        ...this.genFormProps('查看', topic),
        readonly: true,
        title: '课题详情',
        modalProps: { width: '75em' },
      };
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
  getEntityFormPop(formProps?: EntityFormProps) {
    if (formProps && formProps.readonly) {
      return <TopicView {...formProps} />;
    }
    return super.getEntityFormPop(formProps);
  }
}
