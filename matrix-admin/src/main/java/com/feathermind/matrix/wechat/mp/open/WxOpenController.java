package com.feathermind.matrix.wechat.mp.open;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信开放平台登录，暂时和mp共用配置
 */
@Slf4j
@RestController
@RequestMapping("/wechat/open")
public class WxOpenController {
    @Autowired
    WxMpService mpService;

    /**
     * 获取用户信息
     */
    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        try {
            WxOAuth2AccessToken accessToken = mpService.getOAuth2Service().getAccessToken(code);
            log.info("accessToken={}", accessToken);
            WxOAuth2UserInfo wxMpUser = mpService.getOAuth2Service().getUserInfo(accessToken, null);
            log.info("wxMpUser={}", wxMpUser);
        } catch (Exception e) {
            log.error("获取用户信息异常！", e);
        }
    }
}
