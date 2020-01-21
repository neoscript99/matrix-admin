import React from 'react';
import { TopicOperator } from '../topic';
import { topicService } from '../../services';
export class InitialApplyOperator extends TopicOperator {
  getApply(): any {
    return this.props.topic.initialApply;
  }

  afterPass() {
    const { topic } = this.props;
    topicService.save({
      id: topic.id,
      topicStatusCode: 'applied',
    });
  }
}
