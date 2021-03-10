package com.feathermind.matrix.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gorm.wechat.mp")
public class MatrixWxmpConfigProperties {
    private String appId;
    private String appSecret;
    private String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private String qrcodeCreateUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    private String qrcodeShowUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
    private String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getQrcodeCreateUrl() {
        return qrcodeCreateUrl;
    }

    public void setQrcodeCreateUrl(String qrcodeCreateUrl) {
        this.qrcodeCreateUrl = qrcodeCreateUrl;
    }

    public String getQrcodeShowUrl() {
        return qrcodeShowUrl;
    }

    public void setQrcodeShowUrl(String qrcodeShowUrl) {
        this.qrcodeShowUrl = qrcodeShowUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public void setUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }
}
