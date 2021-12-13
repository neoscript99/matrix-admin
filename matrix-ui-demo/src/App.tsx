import React from 'react';
import './App.css';
import { HashRouter } from 'react-router-dom';
import { Login, Home, useServiceStore } from 'matrix-ui-com';
import { config } from './utils';
import { PageSwitch } from './pages';
import logo from './asset/logo.png';
import mainBG from './asset/main_bg.jpg';
import headBG1 from './asset/header_bg1.png';
import headBG2 from './asset/header_bg2.png';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/lib/locale/zh_CN';
import { yard } from './services';

const title = 'Matrix Admin Demo';
const introRender = (
  <>
    <img src="http://attach.neoscript.wang/fmind_mp_qrcode_344.jpg" width={200} />
    <em>羽意软件公众号</em>
  </>
);
const logoRender = (
  <div>
    <img src={logo} height={62} />
    <em className="logo-font" style={{ margin: '0 5px', fontSize: 20 }}>
      {title}
    </em>
  </div>
);
const headerPics = [headBG1, headBG2];
const headerBG = headerPics[Math.round(Math.random())];
const headerCss = { background: `url(${headerBG}) no-repeat center white` };
const footRender = (
  <div>
    <span>Matrix Admin ©2020</span>
    <span>
      &nbsp;(
      <a target="_blank" href="http://site.feathermind.cn/">
        宁波羽意软件
      </a>
      &nbsp;技术支持)
    </span>
  </div>
);
export default function App() {
  const loginStore = useServiceStore(yard.adminServices.loginService);
  return (
    <ConfigProvider locale={zhCN}>
      <HashRouter>
        {loginStore.loginInfo.success ? (
          <Home
            adminServices={yard.adminServices}
            serverRoot={config.serverRoot}
            logoRender={logoRender}
            headerCss={headerCss}
            footRender={footRender}
          >
            <PageSwitch />
          </Home>
        ) : (
          <Login
            adminServices={yard.adminServices}
            title={title}
            introRender={introRender}
            backgroundImage={`url(${mainBG})`}
            demoUsers={config.demoUsers}
            useWechat
            useWxOAuth
          />
        )}
      </HashRouter>
    </ConfigProvider>
  );
}
