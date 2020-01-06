import React from 'react';
import { TopicOperator } from '../topic';
import { applyService, resUserService } from '../../services';
import { ApplyUtil } from '../../utils/ApplyUtil';

export class InitialApplyOperator extends TopicOperator {
  getApply(): any {
    return this.props.topic.initialApply;
  }
}
