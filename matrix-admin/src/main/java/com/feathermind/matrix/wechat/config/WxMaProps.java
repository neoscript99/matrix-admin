package com.feathermind.matrix.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
public class WxMaProps {
    /*设置微信公众号的appid*/
    private String appid;
    /*设置微信公众号的app secret*/
    private String secret;
    /*设置微信公众号的token*/
    private String token;
    /*设置微信公众号的EncodingAESKey*/
    private String aesKey;
    /*消息格式，XML或者JSON*/
    private String msgDataFormat = "JSON";
}
