package com.feathermind.matrix.wechat;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

import java.util.Map;

public interface WxMaBinder {
    /**
     * wxUserInfo 微信用户信息，包含openid
     * @return 绑定成功后的，将用户信息，返回给前台用户
     *  包含：success true/false
     */
    Map bindWxMaUser(WxMaUserInfo userInfo);
    Map wxMaLogin(String openId,String unionId);
}
