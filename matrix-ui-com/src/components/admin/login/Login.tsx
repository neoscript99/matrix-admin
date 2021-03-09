import React, { Component, ReactNode, useCallback, useEffect, useState } from 'react';
import { CodeOutlined, DownOutlined, LockOutlined, UserOutlined } from '@ant-design/icons';
import { Form } from 'antd';
import { Input, Button, Checkbox, Spin, Dropdown, Menu } from 'antd';
import { AdminServices, StringUtil } from 'matrix-ui-service';
import { LoginPage, LoginBox, LoginBoxTitle, LoginBoxItem } from './LoginStyled';
import { useHistory } from 'react-router';
import { commonRules, useServiceStore } from '../../../utils';
import { MenuInfo } from 'rc-menu/lib/interface';
import { useForm } from 'antd/lib/form/Form';

export interface LoginFormProps {
  adminServices: AdminServices;
  title: ReactNode;
  introRender: ReactNode;
  backgroundImage?: any;
  demoUsers?: any[];
  isDev?: boolean;
}
interface S {
  kaptchaId: string;
  kaptchaFree: boolean;
}

/**
 * 通过kaptchaFree方法检查当前用户名、客户端IP是否需要验证码
 * 如果需要，客户端要展示验证码，输入后传给后台
 */
export function Login(props: LoginFormProps) {
  const { adminServices } = props;
  const { title, introRender, backgroundImage, demoUsers, isDev } = props;
  const { loginService } = adminServices;
  const history = useHistory();
  const [kaptchaId, setKaptchaId] = useState('none');
  const [kaptchaFree, setKaptchaFree] = useState(true);
  const [form] = useForm();
  const loginStore = useServiceStore(loginService);
  const { casConfig, lastRoutePath, loginInfo } = loginStore;
  /*检查当前状态是否需要验证码*/
  const checkKaptchaFree = useCallback((e: any, noUser?: boolean) => {
    loginService
      .kaptchaFree(noUser ? null : form.getFieldValue('username'))
      .then((res) => !loginInfo.success && setKaptchaFree(res.success));
  }, []);
  //初始进入检查验证码
  useEffect(() => {
    checkKaptchaFree(null, true);
  }, []);

  useEffect(() => {
    if (loginInfo.success) history.push(lastRoutePath);
  }, [loginStore]);

  const refreshKaptchaId = useCallback(() => {
    //生成一个随机码，作为验证码id，从后台获取新的验证码（不是直接作为验证码）
    setKaptchaId(StringUtil.randomString());
  }, []);

  /**
   * 升级antd4之后代码调整，之前再提交前再次调用form.validateFields进行验证
   */
  const handleSubmit = useCallback(
    (values) => {
      loginService.login({ ...values, isDev }).then((loginInfo) => {
        if (!loginInfo.success) {
          setKaptchaFree(!!loginInfo.kaptchaFree);
          refreshKaptchaId();
        }
      });
    },
    [setKaptchaFree, refreshKaptchaId, isDev],
  );

  const demoUserClick = useCallback(
    ({ key }: MenuInfo) => {
      const item = demoUsers!.find((user) => user.username === key);
      loginService.loginHash({ ...item, isDev });
    },
    [demoUsers, isDev],
  );

  if (loginInfo?.success) return null;

  if (casConfig?.clientEnabled) return <Spin />;
  const css = { color: 'rgba(0,0,0,.25)' };
  return (
    <LoginPage style={{ backgroundImage: backgroundImage }}>
      <LoginBox>
        <LoginBoxItem>
          <LoginBoxTitle>{title}</LoginBoxTitle>
          {introRender}
        </LoginBoxItem>
        <LoginBoxItem>
          <Form form={form} onFinish={handleSubmit} style={{ maxWidth: '300px', marginTop: 30 }} layout="vertical">
            <Form.Item label="用户名" name="username" rules={[{ required: true, message: '用户名不能为空!' }]}>
              <Input
                prefix={<UserOutlined style={css} />}
                size="large"
                autoComplete="username"
                onBlur={checkKaptchaFree}
              />
            </Form.Item>
            <Form.Item label="密码" name="password" rules={[{ required: true, message: '密码不能为空!' }]}>
              <Input prefix={<LockOutlined style={css} />} type="password" size="large" autoComplete="password" />
            </Form.Item>
            {!kaptchaFree && (
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Form.Item label="验证码" name="kaptchaCode" rules={[commonRules.required]}>
                  <Input prefix={<CodeOutlined style={css} />} maxLength={4} size="large" />
                </Form.Item>
                <img
                  src={`${loginService.kaptchaRenderURL}/${kaptchaId}`}
                  height={36}
                  style={{ marginLeft: 5 }}
                  onClick={refreshKaptchaId}
                />
              </div>
            )}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', width: '100%' }}>
              <Form.Item name="remember" valuePropName="checked" initialValue={true} style={{ marginBottom: 0 }}>
                <Checkbox>自动登录</Checkbox>
              </Form.Item>
              {demoUsers && <DemoUserDropdown demoUsers={demoUsers} demoUserClick={demoUserClick} />}
            </div>
            <Button type="primary" htmlType="submit" style={{ width: '100%', marginTop: 10 }}>
              登录
            </Button>
          </Form>
        </LoginBoxItem>
      </LoginBox>
    </LoginPage>
  );
}

interface DemoUserDropdownProps {
  demoUsers: any[];
  demoUserClick: (param: MenuInfo) => void;
}

const DemoUserDropdown = (props: DemoUserDropdownProps) => {
  const { demoUsers, demoUserClick } = props;
  return (
    <Dropdown
      trigger={['click']}
      overlay={
        <Menu onClick={demoUserClick}>
          {demoUsers.map((user) => (
            <Menu.Item key={user.username}>
              {user.name}({user.username})
            </Menu.Item>
          ))}
        </Menu>
      }
    >
      <Button size="small">
        演示登录 <DownOutlined />
      </Button>
    </Dropdown>
  );
};
