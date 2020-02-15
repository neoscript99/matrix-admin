import { ReviewApplyList } from './ReviewApplyList';
import {
  EntityColumnProps,
  DomainService,
  SimpleSearchForm,
  EntityListState,
  EntityListProps,
  EntityFormProps,
} from 'oo-rest-mobx';
import { topicAchieveService } from '../../services';
import { TopicAchieveForm } from './TopicAchieveForm';
import { Button } from 'antd';
import React from 'react';
import { TopicList, TopicView } from '../topic';
const columns: EntityColumnProps[] = [
  { title: '成果名称', dataIndex: 'achieveName' },
  { title: '成果负责人', dataIndex: 'personInCharge.name' },
  ...ReviewApplyList.planColumns,
];
interface S extends EntityListState {
  topicViewProps?: EntityFormProps;
}
export class TopicAchieveList extends ReviewApplyList<EntityListProps, S> {
  get columns(): EntityColumnProps[] {
    return [
      {
        title: '课题',
        dataIndex: 'topic.topicName',
        render: (text, achieve) => (
          <Button type="link" onClick={this.showTopic.bind(this, achieve.topic)}>
            {text}
          </Button>
        ),
      },
      ...columns,
    ];
  }
  render() {
    const topicViewProps = this.state?.topicViewProps;
    return (
      <React.Fragment>
        {topicViewProps && <TopicView {...topicViewProps} />}
        {super.render()}
      </React.Fragment>
    );
  }
  async showTopic(topic: any) {
    const topicViewProps = await TopicList.genTopicViewProps(topic, { onCancel: this.closeTopicView });
    this.setState({ topicViewProps });
  }
  closeTopicView = () => {
    this.setState({ topicViewProps: undefined });
  };
  get domainService(): DomainService {
    return topicAchieveService;
  }

  get reviewTypeCode(): string {
    return 'topic';
  }
  get name() {
    return '课题成果';
  }

  getEntityForm() {
    return TopicAchieveForm;
  }
  getSearchForm() {
    return TopicAchieveSearchForm;
  }
}

export class TopicAchieveSearchForm extends SimpleSearchForm {
  placeholder = '成果名称、所属计划';
}
