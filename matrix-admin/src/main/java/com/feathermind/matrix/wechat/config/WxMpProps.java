package com.feathermind.matrix.wechat.config;

import lombok.Data;

@Data
public class WxMpProps {
    private String appId;
    private String appSecret;
    private String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private String qrcodeCreateUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    private String qrcodeShowUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
    private String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info";
}
