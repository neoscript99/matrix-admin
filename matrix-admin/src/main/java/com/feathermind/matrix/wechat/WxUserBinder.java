package com.feathermind.matrix.wechat;

public interface WxUserBinder<T> {

    /**
     * 微信公众号、小程序、开放平台OAuth用户绑定
     * 支持以下对象
     *
     * @param userInfo
     * @see me.chanjar.weixin.mp.bean.result.WxMpUser
     * @see cn.binarywang.wx.miniapp.bean.WxMaUserInfo
     * @see me.chanjar.weixin.common.bean.WxOAuth2UserInfo
     * @return 是否成功：loginInfo.success
     */
    T bindUser(Object userInfo);

    T login(String openId, String unionId);

    T bindPhone(String openId, String unionId, String phoneNumber);
}
