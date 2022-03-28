import React, { ReactNode, useCallback, useEffect, useMemo, useState } from 'react';
import { UserOutlined } from '@ant-design/icons';
import { Avatar, Button, Layout } from 'antd';
import { useHistory, useLocation, useRouteMatch } from 'react-router';
import { MenuEntity, AdminServices } from 'matrix-ui-service';
import { MenuTree } from '../layout';
import { useServiceStore } from '../../utils';

const { Header, Content, Footer, Sider } = Layout;

export interface HomeProps {
  serverRoot: string;
  adminServices: AdminServices;
  logoRender: ReactNode;
  footRender: ReactNode;
  profilePath?: string;
  headerCss?: React.CSSProperties;
  contentCss?: React.CSSProperties;
  siderCss?: React.CSSProperties;
}
export const Home: React.FC<HomeProps> = (props) => {
  const { adminServices } = props;
  const { logoRender, footRender, headerCss, contentCss, siderCss, profilePath } = props;
  const { loginService, menuService, paramService } = adminServices;
  const history = useHistory();
  const location = useLocation();
  const [collapsed, setCollapsed] = useState(false);

  const onMenuClick = useCallback((menu: MenuEntity) => history.push(`/${menu.app}`), [history]);

  const goProfile = useCallback(
    () => location.pathname !== profilePath && history.push(profilePath),
    [profilePath, location.pathname, history],
  );
  const menuStore = useServiceStore(menuService);
  const loginStore = useServiceStore(loginService);
  const paramStore = useServiceStore(paramService);
  const needChangePassword = useMemo(
    () => loginStore.forcePasswordChange && paramService.getByCode('ChangeInitPassword')?.value === 'true',
    [loginStore, paramStore],
  );
  useEffect(() => {
    needChangePassword && goProfile();
  }, [needChangePassword, location.pathname]);
  const { loginInfo } = loginStore;
  const logout = useCallback(() => loginService.logout(history), [history]);
  const buttonCss: React.CSSProperties = { padding: '3px' };
  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          background: '#fff',
          boxShadow: '0 2px 8px #f0f1f2',
          fontWeight: 'bolder',
          padding: '0 16px',
          lineHeight: 1, //原来设置为64，会导致div>img的高度加大。原来目的是文字垂直居中，已通过flex实现
          ...headerCss,
        }}
      >
        {logoRender}
        <div>
          <Avatar icon={<UserOutlined />} style={{ backgroundColor: '#f56a00' }} />
          <div style={{ display: 'inline-block' }}>
            <span style={{ marginLeft: '0.5rem', lineHeight: '1.2rem' }}>
              {loginInfo?.user?.name || loginInfo.account}(
            </span>
            <Button type="link" onClick={goProfile} style={buttonCss}>
              个人设置
            </Button>
            <span>/</span>
            <Button type="link" onClick={logout} style={buttonCss}>
              退出系统
            </Button>
            <span>)</span>
          </div>
        </div>
      </Header>
      <Layout hasSider>
        <Sider
          collapsible
          collapsed={collapsed}
          onCollapse={setCollapsed}
          theme="light"
          style={{ borderTop: 'solid thin #f3f3f3', borderBottom: 'solid thin #f3f3f3', ...siderCss }}
        >
          <MenuTree rootMenu={menuStore.menuTree} menuClick={onMenuClick} />
        </Sider>
        <Layout>
          <Content
            style={{
              margin: 8,
              padding: 16,
              background: '#fff',
              height: '100%',
              minHeight: 360,
              ...contentCss,
            }}
          >
            {props.children}
          </Content>
          <Footer style={{ textAlign: 'center' }}>{footRender}</Footer>
        </Layout>
      </Layout>
    </Layout>
  );
};

Home.defaultProps = {
  profilePath: '/Profile',
};
