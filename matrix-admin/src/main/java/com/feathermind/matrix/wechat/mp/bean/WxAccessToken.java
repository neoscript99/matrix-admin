package com.feathermind.matrix.wechat.mp.bean;

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
    private int expires_in;
    private LocalDateTime createTime = LocalDateTime.now();
    private String errcode;
    private String errmsg;

    public boolean isValid() {
        return errcode==null && createTime.plusSeconds(expires_in).isAfter(LocalDateTime.now());
    }
}
