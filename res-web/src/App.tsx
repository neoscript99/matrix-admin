import React, { Component } from 'react';
import './App.css';
import { HashRouter, Route, Switch } from 'react-router-dom';
import { Login, LoginFormProps, Home } from 'oo-rest-mobx';
import { adminServices, paramService } from './services';
import { config } from './utils';
import { PageSwitch } from './pages';
import logo from './asset/logo.png';
import { observer } from 'mobx-react';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/lib/locale/zh_CN';
const introRender = (
  <div>
    <p>
      立项管理： <em>立项流程、结题流程管理</em>
    </p>
    <p>
      成果评比： <em>邀请专家对项目成果进行评分、排名</em>
    </p>
    <p>
      论文评比： <em>邀请专家对论文进行评分、排名</em>
    </p>
  </div>
);
const loginPath = '/login/';
const demoUsers = [
  { name: '系统管理员', username: 'sys-admin', password: 'abc000' },
  { name: '单位管理员', username: 'dept-admin', password: 'abc000' },
];
const loginProps: LoginFormProps = {
  adminServices,
  title: '教育科研项目管理系统',
  introRender,
  //https://unsplash.com/photos/asviIGR3CPE
  backgroundImage:
    'url(https://images.unsplash.com/photo-1517673132405-a56a62b18caf?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2112)',
};
const homeProps = {
  adminServices,
  serverLogout: config.serverLogout,
  serverRoot: config.serverRoot,
  logoRender: <img src={logo} />,
  PageSwitch,
  loginPath,
  footRender: (
    <div>
      <span>鄞州教育 ©2020</span>
      <span>
        &nbsp;(
        <a target="_blank" href="http://www.feathermind.cn/">
          宁波羽意软件
        </a>
        &nbsp;技术支持)
      </span>
    </div>
  ),
};

@observer
class App extends Component {
  render() {
    //console.debug('依赖paramService.store.allList：', paramService.store.allList);
    const profiles = paramService.getByCode('EnvironmentProfiles')?.value;
    console.debug('EnvironmentProfiles: ', profiles);
    const users = profiles === 'dev' ? demoUsers : undefined;
    return (
      <ConfigProvider locale={zhCN}>
        <HashRouter>
          <Switch>
            <Route path={loginPath} render={props => <Login {...props} {...loginProps} demoUsers={users} />} />
            <Route render={props => <Home {...props} {...homeProps} />} />
          </Switch>
        </HashRouter>
      </ConfigProvider>
    );
  }
}

export default App;
