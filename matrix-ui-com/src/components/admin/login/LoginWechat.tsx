import React, { useCallback, useEffect } from 'react';
import { WxMpService } from 'matrix-ui-service';
import styled from 'styled-components';
import { Card } from 'antd';
import { useServiceStore } from '../../../utils';

export interface LoginWechatProps {
  wxMpService: WxMpService;
}

export const LoginWechatRefresh = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  color: #fff;
  background-color: #2fb746;
  background-color: rgba(48, 184, 70, 0.9);
  background-image: url(http://attach.neoscript.wang/icon_refresh.png);
  background-repeat: no-repeat;
  background-position: center center;
`;

export function LoginWechat({ wxMpService }: LoginWechatProps) {
  const store = useServiceStore(wxMpService);
  const refresh = useCallback(() => {
    wxMpService.getQrcodeInfo();
  }, []);
  //从后台获取二维码
  useEffect(() => {
    wxMpService.getQrcodeInfo();
    return () => wxMpService.stopBindCheck();
  }, []);
  const { checkTimesUp, qrcodeInfo } = store;
  return (
    <Card onClick={refresh} bodyStyle={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: 12 }}>
      {qrcodeInfo && <img src={qrcodeInfo.wxImgUrl} width={200} />}
      {checkTimesUp && (
        <LoginWechatRefresh>
          <span>二维码失效</span>
          <span>请点击刷新</span>
        </LoginWechatRefresh>
      )}
      <em>微信扫码登录（注册）</em>
    </Card>
  );
}
