package com.feathermind.matrix.wechat.mp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "matrix.wechat")
public class WxMpProperties {
    private String appId;
    private String appSecret;
    private String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private String qrcodeCreateUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    private String qrcodeShowUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
    private String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info";
}