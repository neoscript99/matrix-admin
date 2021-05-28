import { Button, Checkbox, Dropdown, Form, Input, Menu } from 'antd';
import { CodeOutlined, DownOutlined, LockOutlined, UserOutlined } from '@ant-design/icons';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { MenuInfo } from 'rc-menu/lib/interface';
import { useForm } from 'antd/lib/form/Form';
import { AdminServices, StringUtil } from 'matrix-ui-service';
import { commonRules, useServiceStore } from '../../../utils';

export interface LoginFormProps {
  adminServices: AdminServices;
  demoUsers?: { name: string; username: string }[];
}

/**
 * 通过kaptchaFree方法检查当前用户名、客户端IP是否需要验证码
 * 如果需要，客户端要展示验证码，输入后传给后台
 */
export function LoginForm(props: LoginFormProps) {
  const { adminServices, demoUsers } = props;
  const { loginService, paramService } = adminServices;
  const [kaptchaId, setKaptchaId] = useState('none');
  const [kaptchaFree, setKaptchaFree] = useState(true);
  const [form] = useForm();
  const loginStore = useServiceStore(loginService);
  const paramStore = useServiceStore(paramService);
  //依赖paramStore
  const isDev = useMemo(() => paramService.getMatrixConfig()?.devLogin, [paramStore]);

  const { loginInfo } = loginStore;
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
      loginService.loginHash({ ...item, isDev, password: 'none' });
    },
    [demoUsers, isDev],
  );

  if (loginInfo?.success) return null;

  const css = { color: 'rgba(0,0,0,.25)' };
  return (
    <Form form={form} onFinish={handleSubmit} style={{ width: '100%' }} layout="vertical">
      <Form.Item label="用户名" name="username" rules={[{ required: true, message: '用户名不能为空!' }]}>
        <Input prefix={<UserOutlined style={css} />} size="large" autoComplete="username" onBlur={checkKaptchaFree} />
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
        {isDev && demoUsers && <DemoUserDropdown demoUsers={demoUsers} demoUserClick={demoUserClick} />}
      </div>
      <Button type="primary" htmlType="submit" style={{ width: '100%', marginTop: 10 }}>
        登录
      </Button>
    </Form>
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
