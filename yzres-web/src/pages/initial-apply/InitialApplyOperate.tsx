import React, { Component } from 'react';
import { Button, Popconfirm } from 'antd';
import { Entity } from 'oo-rest-mobx';
import { TopicMember } from '../topic';

interface P {
  item: Entity;
}

interface S {
  showMember?: boolean;
}

export class InitialApplyOperate extends Component<P, S> {
  state = {} as S;
  handleMember = () => {
    this.setState({ showMember: true });
  };

  render() {
    const { item } = this.props;
    const { showMember } = this.state;
    const buttonCss: React.CSSProperties = { padding: '0 2px' };
    return (
      <div className="flex-row" style={{ margin: '-10px 0' }}>
        <Button type="link" style={buttonCss} onClick={this.handleMember}>
          课题成员管理
        </Button>
        <TopicMember topic={item.topic} visible={!!showMember} />
        <Button type="link" style={buttonCss}>
          提交审核
        </Button>
        <Popconfirm title="确定删除?" okText="确定" cancelText="取消">
          <Button type="link" style={buttonCss}>
            删除
          </Button>
        </Popconfirm>
      </div>
    );
  }
}
