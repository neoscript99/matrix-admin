import React, { Component } from 'react';
import { Button, Dropdown, Icon, Menu, message, Popconfirm } from 'antd';
import { Entity } from 'oo-rest-mobx';
import { RouteComponentProps } from 'react-router';
import { applyService, resUserService, topicService } from '../../services';
import { ClickParam } from 'antd/lib/menu';
import { checkEditable } from './index';
import { ApproveDropdown } from '../../components';

interface P extends Partial<RouteComponentProps> {
  item: Entity;
  onChange?: (item: Entity) => void;
}

export class InitialApplyOperate extends Component<P> {
  approveMenuClick = ({ key: statusCode }: ClickParam) => {
    const { item } = this.props;
    const apply: any = { id: item.initialApply.id, statusCode };
    if (statusCode === 'pass') {
      apply.passTime = new Date();
      topicService.save({ id: item.id, topicStatusCode: 'applied' });
    }
    applyService.save(apply).then(this.fireChange);
  };
  fireChange = () => {
    const { item, onChange } = this.props;
    message.info(`更新成功`);
    onChange && onChange(item);
  };
  handleMember = () => {
    const { history, item } = this.props;
    topicService.store.currentItem = item;
    history && history.push('/TopicMember');
  };
  handleSubmit = () => {
    const { item } = this.props;
    applyService.save({ id: item.initialApply.id, statusCode: 'wait' }).then(this.fireChange);
  };
  handleDelete = () => {
    const { item } = this.props;
    applyService.delete(item.initialApply.id).then(this.fireChange);
  };

  render() {
    const buttonCss: React.CSSProperties = { padding: '0 2px' };
    const {
      item: { initialApply },
    } = this.props;
    const editable = checkEditable(initialApply.statusCode);
    const approveAble = initialApply.statusCode === 'wait' && resUserService.isMainManager();
    return (
      <div className="flex-row" style={{ margin: '-10px 0' }}>
        {editable && (
          <Button type="link" style={buttonCss} onClick={this.handleMember}>
            课题成员管理
          </Button>
        )}
        {editable && (
          <Button type="link" style={buttonCss} onClick={this.handleSubmit}>
            提交审核
          </Button>
        )}
        {approveAble && <ApproveDropdown onMenuClick={this.approveMenuClick} />}
      </div>
    );
  }
}
