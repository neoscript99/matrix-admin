import React, { Component } from 'react';
import { Button, Card, Icon, Progress } from 'antd';
import { Entity } from 'oo-rest-mobx';
export interface WorkPlanCardProps {
  plan: Entity;
}
export class WorkPlanCard extends Component<WorkPlanCardProps> {
  render() {
    const { plan } = this.props;
    return (
      <Card style={{ width: 300, marginTop: 16 }} actions={[<Button icon="setting">提交申请</Button>]}>
        <Card.Meta
          avatar={<Progress type="dashboard" percent={75} />}
          title={plan.planName}
          description={plan.planYear}
        />
      </Card>
    );
  }
}
