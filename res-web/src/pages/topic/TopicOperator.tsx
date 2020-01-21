import React, { Component } from 'react';
import { Button, message } from 'antd';
import { Entity } from 'oo-rest-mobx';
import { RouteChildrenProps } from 'react-router';
import { applyService, resUserService, topicService } from '../../services';
import { ClickParam } from 'antd/lib/menu';
import { ApproveDropdown } from '../../components';
import { ApplyUtil } from '../../utils/ApplyUtil';
import { ButtonProps } from 'antd/lib/button';

export interface TopicOperatorProps extends Partial<RouteChildrenProps> {
  topic: Entity;
  onChange?: (item: Entity) => void;
}

export abstract class TopicOperator<P extends TopicOperatorProps = TopicOperatorProps> extends Component<P> {
  approveMenuClick({ key: statusCode }: ClickParam) {
    const apply: any = { statusCode };
    if (statusCode === 'pass') {
      apply.passTime = new Date();
      this.afterPass();
    }
    this.saveApply(apply);
  }
  abstract afterPass();
  fireChange() {
    const { topic, onChange } = this.props;
    message.info(`更新成功`);
    onChange && onChange(topic);
  }
  handleMember() {
    const { history, topic } = this.props;
    topicService.store.currentItem = topic;
    history && history.push('/TopicMember');
  }
  submitApply() {
    this.saveApply({ statusCode: 'wait' });
  }

  //保存申请
  saveApply(apply: any) {
    applyService.save({ id: this.getApply()?.id, ...apply }).then(this.fireChange.bind(this));
  }
  getEditable(): boolean {
    return ApplyUtil.checkEditable(this.getApply());
  }
  getApproveAble(): boolean {
    return this.getApply()?.statusCode === 'wait' && resUserService.isMainManager();
  }
  //当前页面要维护的申请信息，
  //立项流程的申请对应：initialApply
  //结题流程的申请对应：finishApply
  getApply(): any {
    return {};
  }
  buttonProps: Partial<ButtonProps> = {
    type: 'primary',
    size: 'small',
    style: { margin: 2 },
  };
  render() {
    const editable = this.getEditable();
    const approveAble = this.getApproveAble();
    return (
      <div className="flex-row" style={{ margin: -10 }}>
        {this.getExtraButton()}
        {editable && (
          <Button {...this.buttonProps} onClick={this.handleMember.bind(this)}>
            课题成员管理
          </Button>
        )}
        {editable && (
          <Button {...this.buttonProps} onClick={this.submitApply.bind(this)}>
            提交审核
          </Button>
        )}
        {approveAble && (
          <ApproveDropdown buttonProps={this.buttonProps} onMenuClick={this.approveMenuClick.bind(this)} />
        )}
      </div>
    );
  }
  getExtraButton(): React.ReactNode {
    return null;
  }
}
