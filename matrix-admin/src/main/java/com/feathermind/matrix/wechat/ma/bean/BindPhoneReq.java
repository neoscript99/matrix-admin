package com.feathermind.matrix.wechat.ma.bean;

import lombok.Data;

@Data
public class BindPhoneReq {
    private String code;
    private String encryptedData;
    private String ivStr;
}
