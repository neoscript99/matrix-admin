import React, { Component } from 'react';
import { Button, Popconfirm } from 'antd';

export class InitialApplyOperate extends Component {
  render() {
    const buttonCss: React.CSSProperties = { padding: '0 2px' };
    return (
      <div className="flex-row" style={{ margin: '-10px 0' }}>
        <Button type="link" style={buttonCss}>
          课题成员管理
        </Button>
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
