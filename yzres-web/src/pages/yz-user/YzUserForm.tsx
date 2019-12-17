import React from 'react';
import { UserForm, commonRules, transforms } from 'oo-rest-mobx';
import { yzUserService } from '../../services';
import { WrappedFormUtils } from 'antd/lib/form/Form';
import { Input, Form, DatePicker } from 'antd';
const { required } = commonRules;

export class YzUserForm extends UserForm {
  get userService() {
    return yzUserService;
  }

  getExtraFormItem(getFieldDecorator: WrappedFormUtils['getFieldDecorator'], formItemCss: React.CSSProperties) {
    return (
      <React.Fragment>
        <Form.Item label="身份证" style={formItemCss}>
          {getFieldDecorator('idCard', {
            rules: [required, { min: 16, max: 18, message: '格式错误' }],
          })(<Input maxLength={18} />)}
        </Form.Item>
        <Form.Item label="生日" style={formItemCss}>
          {getFieldDecorator('birthDay', {
            rules: [{ ...required, type: 'date', transform: transforms.momentToDay }],
          })(<DatePicker />)}
        </Form.Item>
        <Form.Item label="职务职称" style={formItemCss}>
          {getFieldDecorator('title', {
            rules: [required],
          })(<Input maxLength={30} />)}
        </Form.Item>
        <Form.Item label="专业" style={formItemCss}>
          {getFieldDecorator('major', {
            rules: [required],
          })(<Input maxLength={30} />)}
        </Form.Item>
        <Form.Item label="最后学历" style={formItemCss}>
          {getFieldDecorator('degreeCode', {
            rules: [required],
          })(<Input maxLength={20} />)}
        </Form.Item>
      </React.Fragment>
    );
  }
}
