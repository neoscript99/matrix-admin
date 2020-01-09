import React from 'react';
import { TopicOperator, TopicOperatorProps } from '../topic';
import { Button } from 'antd';
interface P extends TopicOperatorProps {
  onStartFinishApply: (topic: any) => void;
}
export class FinishApplyOperator extends TopicOperator<P> {
  get approvedStatus(): string {
    return 'finished';
  }
  getApply(): any {
    return this.props.topic.finishApply;
  }
  startFinishApply() {
    const { onStartFinishApply, topic } = this.props;
    onStartFinishApply(topic);
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
