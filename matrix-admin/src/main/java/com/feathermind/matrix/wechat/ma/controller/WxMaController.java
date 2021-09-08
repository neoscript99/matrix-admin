package com.feathermind.matrix.wechat.ma.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.feathermind.matrix.wechat.WxMaBinder;
import com.feathermind.matrix.wechat.WxPhoneBinder;
import com.feathermind.matrix.wechat.ma.bean.BindPhoneReq;
import com.feathermind.matrix.wechat.ma.bean.LoginReq;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序登录流程
 */
@Slf4j
@RestController
@RequestMapping("/wechat/ma")
public class WxMaController {
    @Autowired(required = false)
    WxMaBinder wxMaBinder;
    @Autowired(required = false)
    WxMaService wxMaService;
    @Autowired(required = false)
    WxPhoneBinder wxPhoneBinder;
    private final Map FAIL = new HashMap<String, Boolean>() {{
        put("success", false);
    }};

    @PostMapping("/wxMaLogin")
    Map wxMaLogin(@RequestBody LoginReq req) {
        if (wxMaBinder == null)
            return FAIL;
        WxMaJscode2SessionResult result = getSessionInfo(req.getCode());
        return wxMaBinder.wxMaLogin(result.getOpenid(), result.getUnionid());
    }

    @PostMapping("/bindUser")
    Map bindUser(@RequestBody WxMaUserInfo userInfo) {
        if (wxMaBinder == null)
            return FAIL;
        return wxMaBinder.bindWxMaUser(userInfo);
    }

    @PostMapping("/bindPhone")
    Map bindPhone(@RequestBody BindPhoneReq req) {
        if (wxPhoneBinder == null)
            return FAIL;

        WxMaJscode2SessionResult result = getSessionInfo(req.getCode());
        WxMaPhoneNumberInfo phoneNumberInfo =
                wxMaService.getUserService()
                        .getPhoneNoInfo(result.getSessionKey(), req.getEncryptedData(), req.getIvStr());
        if (phoneNumberInfo == null) {
            return FAIL;
        }
        return wxPhoneBinder.bindWxPhone(result.getOpenid(), result.getUnionid(), phoneNumberInfo.getPurePhoneNumber());
    }

    private WxMaJscode2SessionResult getSessionInfo(String code) {
        try {
            return wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            log.error("微信接口getSessionInfo错误：", e);
            throw new RuntimeException(e);
        }
    }
}
