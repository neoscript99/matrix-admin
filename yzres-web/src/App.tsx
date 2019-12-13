import React, { Component } from 'react';
import './App.css';
import { HashRouter, Route, Switch } from 'react-router-dom';
import { Login, LoginFormProps, Home } from 'oo-rest-mobx';
import { adminServices } from './services';
import { config } from './utils';
import { PageSwitch } from './pages';
const introRender = (
  <div>
    <p>
      项目立项： <em>各单位提交立项申请</em>
    </p>
    <p>
      成果评比： <em>专家对项目成果进行评分、排名</em>
    </p>
    <p>
      论文评比： <em>专家对论文进行评分、排名</em>
    </p>
  </div>
);
const loginPath = '/login/';
const loginProps: Partial<LoginFormProps> = {
  adminServices,
  title: '鄞州区教育局科研项目管理系统',
  introRender,
};
const homeProps = {
  adminServices,
  serverLogout: config.serverLogout,
  serverRoot: config.serverRoot,
  logoRender: <div className="page-head-logo" />,
  PageSwitch,
  loginPath,
  footRender: '鄞州教育 ©2020',
};
class App extends Component {
  render() {
    return (
      <HashRouter>
        <Switch>
          <Route path={loginPath} render={props => <Login {...props} {...loginProps} />} />
          <Route render={props => <Home {...props} {...homeProps} />} />
        </Switch>
      </HashRouter>
    );
  }
}

export default App;
