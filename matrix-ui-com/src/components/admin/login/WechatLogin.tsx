import React, { useEffect } from 'react';
import { WechatService } from 'matrix-ui-service';
import { useServiceStore } from '../../../utils';

export interface WechatLoginProps {
  wechatService: WechatService;
  loadingRender: React.ReactNode;
}

export function WechatLogin({ wechatService, loadingRender }: WechatLoginProps) {
  const store = useServiceStore(wechatService);
  //从后台获取二维码
  useEffect(() => {
    //延迟获取二维码
    setTimeout(() => {
      wechatService.getQrcodeInfo();
    }, 3000);
  }, []);

  return (
    <div style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }}>
      {store.qrValid ? <img src={store.qrcodeInfo.wxImgUrl} width={200} /> : loadingRender}
      <em>使用手机微信扫码登录（注册）</em>
    </div>
  );
}
