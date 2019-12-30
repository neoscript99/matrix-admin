import React, { Component } from 'react';
import { Button, Popconfirm } from 'antd';
import { Entity } from 'oo-rest-mobx';
import { RouteComponentProps } from 'react-router';
import { topicService } from '../../services';

interface P extends Partial<RouteComponentProps> {
  item: Entity;
}
export class InitialApplyOperate extends Component<P> {
  handleMember = () => {
    const {
      history,
      item: { topic },
    } = this.props;
    topicService.store.currentItem = topic;
    history && history.push('/TopicMember');
  };

  render() {
    const { item } = this.props;
    const buttonCss: React.CSSProperties = { padding: '0 2px' };
    return (
      <div className="flex-row" style={{ margin: '-10px 0' }}>
        <Button type="link" style={buttonCss} onClick={this.handleMember}>
          课题成员管理
        </Button>
        {item.statusCode === 'draft' && (
          <Button type="link" style={buttonCss}>
            提交审核
          </Button>
        )}
        {item.statusCode === 'draft' && (
          <Popconfirm title="确定删除?" okText="确定" cancelText="取消">
            <Button type="link" style={buttonCss}>
              删除
            </Button>
          </Popconfirm>
        )}
      </div>
    );
  }
}
