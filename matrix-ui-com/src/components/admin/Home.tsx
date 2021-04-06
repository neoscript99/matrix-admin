import React, { ComponentType, ReactNode, useCallback, useMemo, useState } from 'react';
import { UserOutlined } from '@ant-design/icons';
import { Avatar, Button, Layout } from 'antd';
import { useHistory, useLocation, useRouteMatch } from 'react-router';
import { MenuEntity, AdminServices } from 'matrix-ui-service';
import { MenuTree } from '../layout';
import { useServiceStore } from '../../utils';

const { Header, Content, Footer, Sider } = Layout;

export interface HomeProps {
  serverLogout: boolean;
  serverRoot: string;
  adminServices: AdminServices;
  logoRender: ReactNode;
  footRender: ReactNode;
  PageSwitch: ComponentType<PageSwitchProps>;
  profilePath?: string;
  headerCss?: React.CSSProperties;
  contentCss?: React.CSSProperties;
  siderCss?: React.CSSProperties;
}

export interface PageSwitchProps {
  pathPrefix: string;
}

export function Home(props: HomeProps) {
  const { serverLogout, serverRoot, PageSwitch, adminServices } = props;
  const { logoRender, footRender, headerCss, contentCss, siderCss, profilePath } = props;
  const { loginService, menuService, paramService } = adminServices;
  const history = useHistory();
  const location = useLocation();
  const match = useRouteMatch();
  const [collapsed, setCollapsed] = useState(false);

  const pathPrefix = useMemo(() => match.path, [match]);
  const onMenuClick = useCallback((menu: MenuEntity) => history.push(`${pathPrefix}${menu.app}`), [
    history,
    pathPrefix,
  ]);

  const logout = useCallback(() => {
    loginService.logout();
    if (serverLogout) window.location.href = `${serverRoot}/logout`;
    else history.push('/');
  }, [serverLogout, loginService]);

  const goProfile = useCallback(() => {
    const pathname = profilePath || '/Profile';
    if (location.pathname !== pathname) {
      console.debug('Home.goProfile: ', location.pathname, pathname);
      history.push(pathname);
      return true;
    }
    return false;
  }, [profilePath, location, history]);
  const menuStore = useServiceStore(menuService);
  const loginStore = useServiceStore(loginService);
  const paramStore = useServiceStore(paramService);
  const needChangePassword = useMemo(
    () => loginStore.forcePasswordChange && paramService.getByCode('ChangeInitPassword')?.value === 'true',
    [loginStore, paramStore],
  );
  const { loginInfo } = loginStore;
  if (needChangePassword && goProfile()) {
    return null;
  }
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
            <PageSwitch pathPrefix={pathPrefix} />
          </Content>
          <Footer style={{ textAlign: 'center' }}>{footRender}</Footer>
        </Layout>
      </Layout>
    </Layout>
  );
}
