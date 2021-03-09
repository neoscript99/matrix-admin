import React, { useCallback, useState } from 'react';
import { AdminPageProps } from '../';
import { Form, Button, Card, message, FormItemProps } from 'antd';
import { commonRules, useServiceStore } from '../../../utils';
import { CheckboxChangeEvent, CheckboxProps } from 'antd/lib/checkbox';
import { Entity, StringUtil, UserEntity } from 'matrix-ui-service';
import { CheckboxField, InputField } from '../../../ant-design-field';

const { required, email, cellPhone, password } = commonRules;

export function UserProfile(props: AdminPageProps) {
  const { services } = props;
  const [showPassword, setShowPassword] = useState<boolean>();
  const handleCheckboxChange = useCallback((e: CheckboxChangeEvent) => setShowPassword(e.target.checked), []);
  const loginStore = useServiceStore(services.loginService);
  const handleSave = useCallback((item: UserEntity) => {
    if (item.password === services.loginService.initPasswordHash) message.error('新密码不能和初始密码相同');
    else
      services.userService.save(item).then((item) => {
        //todo 更新后处理需改造
        services.loginService.store.forcePasswordChange = false;
        services.loginService.store.loginInfo.user = item as UserEntity;
        services.loginService.fireStoreChange();
        message.success('保存成功');
      });
  }, []);

  const {
    loginInfo: { user },
    forcePasswordChange,
  } = loginStore;
  const showPasswordFinally = forcePasswordChange || showPassword;
  return (
    <div style={{ display: 'flex', justifyContent: 'space-around' }}>
      <Card title={`${user?.name} 个人信息设置`} style={{ width: '40rem' }}>
        <ProfileFrom
          inputItem={user}
          showPassword={showPasswordFinally}
          onCheckboxChange={handleCheckboxChange}
          onSave={handleSave}
        />
      </Card>
    </div>
  );
}

interface ProfileFormProps {
  inputItem: UserEntity;
  showPassword: boolean;
  onCheckboxChange: CheckboxProps['onChange'];
  onSave: (item: Entity) => void;
}

export function ProfileFrom(props: ProfileFormProps) {
  const { onSave, inputItem } = props;
  const [firstPassword, setFirstPassword] = useState<string>();
  //todo 个人设置接口应该单独开发，防止恶意修改“帐号”等重要信息
  const handleSubmit = useCallback(
    (saveItem) => {
      if (saveItem.showPassword) {
        if (saveItem.password === saveItem.passwordAgain)
          onSave({ id: inputItem.id, ...saveItem, password: StringUtil.sha256(saveItem.password) });
        else message.error('两次输入的密码不一致');
      } else onSave({ id: inputItem.id, ...saveItem });
    },
    [onSave, inputItem],
  );
  const itemCol: Pick<FormItemProps, 'labelCol' | 'wrapperCol'> = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  };
  return (
    <Form {...itemCol} onFinish={handleSubmit} initialValues={inputItem}>
      <InputField formItemProps={{ label: '单位' }} value={inputItem?.dept.name} readonly />
      <InputField formItemProps={{ label: '帐号' }} value={inputItem?.account} readonly />
      <InputField fieldId="name" formItemProps={{ label: '姓名', rules: [required] }} maxLength={10} minLength={2} />
      <InputField
        fieldId="cellPhone"
        formItemProps={{ label: '手机号码', rules: [required, cellPhone] }}
        maxLength={11}
      />
      <InputField fieldId="email" formItemProps={{ label: '电子邮箱', rules: [email] }} maxLength={32} />
      <CheckboxField fieldId="showPassword" formItemProps={{ label: '修改密码', initialValue: false }} />
      {/*嵌套的form item，样式需特殊处理*/}
      <Form.Item dependencies={['showPassword']} labelCol={{ span: 0 }} wrapperCol={{ span: 24 }}>
        {(form) =>
          form.getFieldValue('showPassword') && (
            <React.Fragment>
              <InputField
                fieldId="password"
                allowClear={true}
                type="password"
                formItemProps={{ label: '密码', ...itemCol, rules: [required, password] }}
                maxLength={16}
                onChange={(e) => setFirstPassword(e.target.value)}
              />
              <InputField
                fieldId="passwordAgain"
                allowClear={true}
                type="password"
                formItemProps={{
                  label: '密码确认',
                  ...itemCol,
                  rules: [required, { type: 'enum', enum: [firstPassword], message: '密码不一致' }],
                }}
                maxLength={16}
              />
            </React.Fragment>
          )
        }
      </Form.Item>
      <Form.Item wrapperCol={{ span: 16, offset: 8 }}>
        <Button type="primary" htmlType="submit">
          提交
        </Button>
      </Form.Item>
    </Form>
  );
}
