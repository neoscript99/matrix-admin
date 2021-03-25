package com.feathermind.matrix.wechat.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 微信AccessToken，支持有效性检查
 */
@Data
public class WxAccessToken {
    //获取到的凭证
    private String access_token;
    //凭证有效时间，单位：秒
    private Integer expires_in;
    private LocalDateTime createTime = LocalDateTime.now();

    public boolean isValid() {
        return createTime.plusSeconds(expires_in).isAfter(LocalDateTime.now());
    }
}
