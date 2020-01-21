import React from 'react';
import { TopicOperator, TopicOperatorProps } from '../topic';
import { Button } from 'antd';
import { topicService } from '../../services';
import moment from 'moment';
import { DatePickerField } from 'oo-rest-mobx';
interface P extends TopicOperatorProps {
  onStartFinishApply: (topic: any) => void;
}
export class FinishApplyOperator extends TopicOperator<P> {
  afterPass() {
    const { topic } = this.props;
    topicService.save({
      id: topic.id,
      topicStatusCode: 'finished',
      finishDay: moment().format(DatePickerField.DEFAULT_DATE_FORMAT),
      topicCert: `${topic.initialCode}JT`,
    });
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
        <Button {...this.buttonProps} onClick={this.startFinishApply.bind(this)}>
          发起结题申请
        </Button>
      )
    );
  }
}
