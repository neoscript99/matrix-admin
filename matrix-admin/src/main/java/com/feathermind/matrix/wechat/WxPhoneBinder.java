package com.feathermind.matrix.wechat;

import java.util.Map;

public interface WxPhoneBinder {
    Map bindWxPhone(String openId, String unionId, String phoneNumber);
}
