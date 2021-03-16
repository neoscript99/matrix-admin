package com.feathermind.matrix.bean;

import lombok.Data;

@Data
public class WxBindReq {
    private String scene_str;
    /**
     * todo 如果传入userId，就是已登录用户做绑定处理
     */
    private String userId;
}
