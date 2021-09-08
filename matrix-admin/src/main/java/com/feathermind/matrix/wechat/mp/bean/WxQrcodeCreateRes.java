package com.feathermind.matrix.wechat.mp.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WxQrcodeCreateRes {
    //微信二维码
    private String wxImgUrl;
    //获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
    private String ticket;
    //该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。
    private int expire_seconds;
    //二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
    private String url;
    //场景值一般为随机字符串，需要返回前台
    private String scene_str;
    private String error;

    private LocalDateTime createTime = LocalDateTime.now();

    public boolean isValid() {
        return createTime.plusSeconds(expire_seconds).isAfter(LocalDateTime.now());
    }
}
