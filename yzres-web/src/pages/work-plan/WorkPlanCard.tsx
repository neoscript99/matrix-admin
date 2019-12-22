import React, { Component } from 'react';
import { Icon, Card, Progress } from 'antd';
import { Entity } from 'oo-rest-mobx';
import moment from 'moment';
export interface WorkPlanCardProps {
  plan: Entity;
}
export class WorkPlanCard extends Component<WorkPlanCardProps> {
  render() {
    const { plan } = this.props;
    const begin = moment(plan.planBeginDay);
    const past = moment().diff(begin, 'day') + 1;
    const total = moment(plan.planEndDay).diff(begin, 'day') + 1;
    const percent = Math.floor((past * 100) / total);
    console.debug('percent: ', past, total, percent);
    const action = (
      <div>
        <Icon type="form" /> 提交申请
      </div>
    );
    return (
      <Card style={{ width: 300 }} actions={[action]}>
        <Card.Meta
          avatar={<Progress type="dashboard" width={50} percent={percent} />}
          title={<p style={{ whiteSpace: 'normal' }}>{plan.planName}</p>}
          description={`${plan.planBeginDay}~${plan.planEndDay}`}
        />
      </Card>
    );
  }
}
