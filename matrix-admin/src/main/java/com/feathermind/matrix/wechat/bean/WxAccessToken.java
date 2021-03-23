package com.feathermind.matrix.wechat.bean;

import lombok.Data;

import java.time.LocalTime;

/**
 * 微信AccessToken，支持有效性检查
 */
@Data
public class WxAccessToken {
    //获取到的凭证
    private String access_token;
    //凭证有效时间，单位：秒
    private Integer expires_in;
    private LocalTime createTime = LocalTime.now();

    public boolean isValid() {
        return createTime.plusSeconds(expires_in).isAfter(LocalTime.now());
    }
}
