import React, { Component } from 'react';
import { Button, Dropdown, Icon, Menu, message, Popconfirm } from 'antd';
import { Entity } from 'oo-rest-mobx';
import { RouteComponentProps } from 'react-router';
import { initialApplyService, resUserService, topicService } from '../../services';
import { ClickParam } from 'antd/lib/menu';

interface P extends Partial<RouteComponentProps> {
  item: Entity;
  onChange?: (item: Entity) => void;
}
const editableStatuses = ['draft', 'repair'];
export class InitialApplyOperate extends Component<P> {
  approveMenuClick = ({ key: statusCode }: ClickParam) => {
    const { item } = this.props;
    initialApplyService.save({ id: item.id, statusCode }).then(this.fireChange);
  };
  fireChange = () => {
    const { item, onChange } = this.props;
    message.info(`更新成功`);
    onChange && onChange(item);
  };
  handleMember = () => {
    const {
      history,
      item: { topic },
    } = this.props;
    topicService.store.currentItem = topic;
    history && history.push('/TopicMember');
  };
  handleSubmit = () => {
    const { item } = this.props;
    initialApplyService.save({ id: item.id, statusCode: 'wait' }).then(this.fireChange);
  };
  handleDelete = () => {
    const { item } = this.props;
    initialApplyService.delete(item.id).then(this.fireChange);
  };
  render() {
    const buttonCss: React.CSSProperties = { padding: '0 2px' };
    const { item } = this.props;
    const editable = editableStatuses.includes(item.statusCode);
    const approveAble = item.statusCode === 'wait' && resUserService.isMainManager();
    return (
      <div className="flex-row" style={{ margin: '-10px 0' }}>
        <Button type="link" style={buttonCss}>
          查看详情
        </Button>
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
        {editable && (
          <Popconfirm title="确定删除?" okText="确定" cancelText="取消" onConfirm={this.handleDelete}>
            <Button type="link" style={buttonCss}>
              删除
            </Button>
          </Popconfirm>
        )}
        {approveAble && (
          <Dropdown
            trigger={['click']}
            overlay={
              <Menu onClick={this.approveMenuClick}>
                <Menu.Item key="repair">退回修改</Menu.Item>
                <Menu.Item key="fail">通不过</Menu.Item>
                <Menu.Item key="pass">通过</Menu.Item>
              </Menu>
            }
          >
            <Button type="link">
              审批 <Icon type="down" />
            </Button>
          </Dropdown>
        )}
      </div>
    );
  }
}
