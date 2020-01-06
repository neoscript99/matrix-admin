import React from 'react';
import { TopicOperator } from '../topic';
import { Button } from 'antd';
import { finishApplyService, loginService, topicService } from '../../services';

export class FinishApplyOperator extends TopicOperator {
  getApply(): any {
    return this.props.topic.finishApply;
  }
  async startFinishApply() {
    const { topic } = this.props;
    const user = { id: loginService.user!.id };
    const finishApply = await this.saveApply({
      name: `${topic.topicName}结题申请`,
      type: 'topic_finish_apply',
      applier: user,
      statusCode: 'draft',
    });
    finishApplyService.save({ id: topic.id, finishApply }).then(this.fireChange.bind(this));
  }
  getExtraButton(): React.ReactNode {
    return (
      !this.props.topic.finishApply && (
        <Button type="link" style={this.buttonCss} onClick={this.startFinishApply.bind(this)}>
          发起结题申请
        </Button>
      )
    );
  }
}
