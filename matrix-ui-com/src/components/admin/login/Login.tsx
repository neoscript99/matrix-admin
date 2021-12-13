import React, { ReactNode, useMemo, useState } from 'react';
import { Tabs, Spin } from 'antd';
import { LoginPage, LoginBox, LoginBoxTitle, LoginBoxItem } from './LoginStyled';
import { useServiceStore } from '../../../utils';
import { LoginWechat } from './LoginWechat';
import { LoginForm, LoginFormProps } from './LoginForm';
import { EditFilled, WechatFilled } from '@ant-design/icons';

export interface LoginProps extends LoginFormProps {
  title: ReactNode;
  introRender: ReactNode;
  backgroundImage?: any;
  useWechat?: boolean;
}

export function Login(props: LoginProps) {
  const { adminServices, useWechat, demoUsers, title, introRender, backgroundImage } = props;
  const { loginService, wxMpService, paramService } = adminServices;
  const [type, setType] = useState<string>('account');
  const loginStore = useServiceStore(loginService);
  const paramStore = useServiceStore(paramService);
  //依赖paramStore
  const casClientEnabled = useMemo(() => paramService.getMatrixConfig()?.casClientEnabled, [paramStore]);
  if (loginStore.loginInfo?.success) return null;
  //如果开启了cas登录，不显示登录框，等待sessionLogin结果
  if (casClientEnabled) return <Spin />;
  return (
    <LoginPage style={{ backgroundImage: backgroundImage }}>
      <LoginBox>
        <LoginBoxItem>
          <LoginBoxTitle>{title}</LoginBoxTitle>
          {introRender}
        </LoginBoxItem>
        <LoginBoxItem>
          <Tabs activeKey={type} onChange={setType} style={{ alignSelf: 'stretch' }}>
            <Tabs.TabPane
              key="account"
              tab={
                <>
                  <EditFilled />
                  密码登录
                </>
              }
            />
            {useWechat && (
              <Tabs.TabPane
                key="wechat"
                tab={
                  <>
                    <WechatFilled />
                    微信登录
                  </>
                }
              />
            )}
          </Tabs>
          {type === 'account' && <LoginForm adminServices={adminServices} demoUsers={demoUsers} />}
          {type === 'wechat' && <LoginWechat wxMpService={wxMpService} />}
        </LoginBoxItem>
      </LoginBox>
    </LoginPage>
  );
}
