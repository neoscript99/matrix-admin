import React, { Component } from 'react';
import { Button, Dropdown, Icon, Menu } from 'antd';
import { ClickParam } from 'antd/lib/menu';
import { DropDownProps } from 'antd/lib/dropdown';
interface P extends Partial<DropDownProps> {
  onMenuClick: (param: ClickParam) => void;
}
export class ApproveDropdown extends Component<P> {
  render() {
    const { onMenuClick, ...dropdownProps } = this.props;
    return (
      <Dropdown
        trigger={['click']}
        overlay={
          <Menu onClick={onMenuClick}>
            <Menu.Item key="repair">退回修改</Menu.Item>
            <Menu.Item key="fail">通不过</Menu.Item>
            <Menu.Item key="pass">通过</Menu.Item>
          </Menu>
        }
        {...dropdownProps}
      >
        <Button type="link">
          审批 <Icon type="down" />
        </Button>
      </Dropdown>
    );
  }
}
