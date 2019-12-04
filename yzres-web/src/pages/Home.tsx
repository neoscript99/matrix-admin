/* eslint-disable @typescript-eslint/no-use-before-define */
import React, { Component } from 'react';
import { Avatar, Button, Layout, Popover } from 'antd';
import { observer } from 'mobx-react';
import * as H from 'history';
import { match as Match, Redirect, Route, Switch } from 'react-router-dom';
import {
  MenuTree,
  RoleList,
  UserList,
  DeptList,
  Note,
  Param,
  UserProfile,
  MenuEntity,
  OperatorSwitch,
  UserRoleList,
} from 'oo-rest-mobx';
import { adminServices } from '../services';
import { config } from '../utils';
import { Welcome } from './Welcome';

const { menuService, userService } = adminServices;

const { Header, Content, Footer, Sider } = Layout;

interface P {
  history: H.History;
  location: H.Location;
  match: Match;
}

@observer
export class Home extends Component<P, { collapsed: boolean }> {
  state = {
    collapsed: false,
  };

  onCollapse = (collapsed: boolean) => {
    this.setState({ collapsed });
  };

  onMenuClick = (menu: MenuEntity) => {
    this.props.history.push(`${this.props.match.path}${menu.app}`);
  };

  logout() {
    userService.clearLoginInfoLocal();
    if (config.serverLogout) window.location.href = `${config.serverRoot}/logout`;
    else {
      userService.logout();
      //清除store缓存信息,例如needRefresh等
      window.location.reload();
    }
  }

  render() {
    const { location, match } = this.props;
    const pathPrefix = match.path;
    const { store: menuStore } = menuService;
    const { loginInfo } = userService.store;
    if (!loginInfo.success) {
      userService.store.lastRoutePath = location.pathname;
      return <Redirect to="/login/" />;
    }
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Header className="page-head" style={{ alignItems: 'center' }}>
          <div className="page-head-logo" />
          <div>
            <Avatar icon="user" style={{ backgroundColor: '#f56a00' }} />
            <Popover
              content={
                <Button type="link" onClick={this.logout.bind(this)}>
                  退出系统
                </Button>
              }
              placement="bottom"
              trigger="hover"
            >
              <span style={{ margin: '0 0.5rem', lineHeight: '1.2rem' }}>{loginInfo.account}</span>
            </Popover>
          </div>
        </Header>
        <Layout hasSider>
          <Sider
            collapsible
            collapsed={this.state.collapsed}
            onCollapse={this.onCollapse}
            theme="light"
            className="page-side"
          >
            <MenuTree rootMenu={menuStore.menuTree} menuClick={this.onMenuClick} />
          </Sider>
          <Layout>
            <Content style={{ margin: '0.8rem', padding: '1rem', background: '#fff', height: '100%', minHeight: 360 }}>
              <Switch>
                <Route path={`${pathPrefix}Role/`} render={() => <RoleList services={adminServices} name="角色" />} />
                <Route
                  path={`${pathPrefix}UserRole/`}
                  render={() => <UserRoleList services={adminServices} name="用户角色" />}
                />
                <Route
                  path={`${pathPrefix}User/`}
                  render={() => <UserList services={adminServices} operatorVisible={allOp} name="用户" />}
                />
                <Route
                  path={`${pathPrefix}Dept/`}
                  render={() => (
                    <DeptList services={adminServices} operatorVisible={{ update: true, create: true }} name="机构" />
                  )}
                />
                <Route path={`${pathPrefix}Note/`} render={() => <Note services={adminServices} name="通知" />} />
                <Route path={`${pathPrefix}Param/`} render={() => <Param services={adminServices} name="参数" />} />
                <Route path={`${pathPrefix}Profile/`} render={() => <UserProfile services={adminServices} />} />
                <Route render={() => <Welcome />} />
              </Switch>
            </Content>
            <Footer style={{ textAlign: 'center' }}>鄞州教育 ©2020</Footer>
          </Layout>
        </Layout>
      </Layout>
    );
  }
}

const allOp: OperatorSwitch = { create: true, update: true, delete: true };
