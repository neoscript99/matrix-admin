package com.feathermind.matrix.wechat;

import com.feathermind.matrix.wechat.bean.WxUserInfo;

import java.util.Map;

public interface WechatBinder {
    /**
     * wxUserInfo 微信用户信息，包含openid
     * @return 绑定成功后的，将用户信息，返回给前台用户
     *  包含：success true/false
     */
    Map bindWechat(WxUserInfo wxUserInfo);
}
