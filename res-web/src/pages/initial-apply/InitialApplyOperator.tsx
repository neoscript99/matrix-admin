import React from 'react';
import { TopicOperator } from '../topic';
export class InitialApplyOperator extends TopicOperator {
  getApply(): any {
    return this.props.topic.initialApply;
  }

  get approvedStatus(): string {
    return 'applied';
  }
}
