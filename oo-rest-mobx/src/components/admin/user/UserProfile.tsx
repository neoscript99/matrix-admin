import React, { Component } from 'react';
import { AdminPageProps } from '../AdminServices';
import { Button, Card, Checkbox, Form, Input, message } from 'antd';
import { FormComponentProps } from 'antd/lib/form';
import { commonRules, genRules } from '../../../utils';
import { EntityForm } from '../../layout';
import { CheckboxChangeEvent, CheckboxProps } from 'antd/lib/checkbox';
import { Entity, UserEntity } from '../../../services';
import { sha256 } from 'js-sha256';
import { InputField } from '../../../ant-design-field';

const { required, email } = commonRules;
interface S {
  showPassword: boolean;
}
export class UserProfile extends Component<AdminPageProps, S> {
  handleCheckboxChange(e: CheckboxChangeEvent) {
    this.setState({ showPassword: e.target.checked });
  }
  handleSave(item: Entity) {
    const { services } = this.props;
    services.userService.save(item).then(item => {
      services.userService.store.loginInfo.user = item as UserEntity;
      message.success('保存成功');
    });
  }
  render() {
    const { services } = this.props;
    const { user } = services.userService.store.loginInfo;
    const showPassword = this.state && this.state.showPassword;
    return (
      <div style={{ display: 'flex', justifyContent: 'space-around' }}>
        <Card title={`${user?.name} 个人信息设置`} style={{ width: '40rem' }}>
          <ProfileFromWrapper
            inputItem={user}
            showPassword={showPassword}
            onCheckboxChange={this.handleCheckboxChange.bind(this)}
            onSave={this.handleSave.bind(this)}
          />
        </Card>
      </div>
    );
  }
}

interface ProfileFormProps extends FormComponentProps {
  inputItem: any;
  showPassword: boolean;
  onCheckboxChange: CheckboxProps['onChange'];
  onSave: (item: Entity) => void;
}

class ProfileFrom extends Component<ProfileFormProps> {
  handleSubmit() {
    const { form, onSave, showPassword, inputItem } = this.props;
    form.validateFields((err, saveItem) => {
      if (!err) {
        if (showPassword) {
          if (saveItem.password === saveItem.passwordAgain)
            onSave({ id: inputItem.id, ...saveItem, password: sha256(saveItem.password) });
          else message.error('两次输入的密码不一致');
        } else onSave({ id: inputItem.id, ...saveItem });
      }
    });
  }
  render() {
    const { form, showPassword, onCheckboxChange } = this.props;
    const { getFieldDecorator } = form;
    return (
      <Form labelCol={{ span: 8 }} wrapperCol={{ span: 16 }}>
        <InputField fieldId="phoneNumber" formItemProps={{ label: '联系电话' }} formUtils={form} maxLength={16} />
        <InputField
          fieldId="email"
          formItemProps={{ label: '电子邮箱' }}
          formUtils={form}
          maxLength={32}
          decorator={{
            rules: [email],
          }}
        />
        <Form.Item label="修改密码">
          <Checkbox checked={showPassword} onChange={onCheckboxChange} />
        </Form.Item>
        {showPassword && (
          <InputField
            fieldId="password"
            allowClear={true}
            type="password"
            formUtils={form}
            formItemProps={{ label: '密码' }}
            maxLength={16}
            decorator={{
              rules: [genRules.minString(4)],
            }}
          />
        )}
        {showPassword && (
          <Form.Item label="密码确认">
            {getFieldDecorator('passwordAgain', {
              rules: [required],
            })(<Input maxLength={16} type="password" allowClear={true} />)}
          </Form.Item>
        )}
        <Form.Item
          wrapperCol={{
            span: 16,
            offset: 8,
          }}
        >
          <Button type="primary" onClick={this.handleSubmit.bind(this)}>
            提交
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

const ProfileFromWrapper = EntityForm.formWrapper(ProfileFrom);
