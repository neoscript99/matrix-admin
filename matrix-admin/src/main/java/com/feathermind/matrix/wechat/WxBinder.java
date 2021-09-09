package com.feathermind.matrix.wechat;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.feathermind.matrix.wechat.mp.bean.WxUserInfo;

public interface WxBinder<T> {
    /**
     * wxUserInfo 微信用户信息，包含openid
     * @return 绑定成功后的，将用户信息，返回给前台用户
     *  包含：success true/false
     */
    T bindWxMpUser(WxUserInfo wxUserInfo);
    /**
     * wxMaUserInfo 微信小程序用户信息，包含openId
     * @return 绑定成功后的，将用户信息，返回给前台用户
     *  包含：success true/false
     */
    T bindWxMaUser(WxMaUserInfo wxMaUserInfo);
    T wxMaLogin(String openId,String unionId);
    T bindWxPhone(String openId, String unionId, String phoneNumber);
}
