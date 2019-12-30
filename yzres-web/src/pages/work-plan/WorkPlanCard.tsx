import React, { Component } from 'react';
import { Card, Progress, Button } from 'antd';
import { Entity } from 'oo-rest-mobx';
import moment from 'moment';
export interface WorkPlanCardProps {
  plan: Entity;
  onApply: (plan: Entity) => void;
}
export class WorkPlanCard extends Component<WorkPlanCardProps> {
  handleApply() {
    const { plan, onApply } = this.props;
    onApply(plan);
  }
  render() {
    const { plan } = this.props;
    const begin = moment(plan.planBeginDay);
    const past = moment().diff(begin, 'day');
    const total = moment(plan.planEndDay).diff(begin, 'day') + 1;
    const percent = Math.floor((past * 100) / total);
    const action = (
      <div>
        <Button icon="form" onClick={this.handleApply.bind(this)}>
          提交申请
        </Button>
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
