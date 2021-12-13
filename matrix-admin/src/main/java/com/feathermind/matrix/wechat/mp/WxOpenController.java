package com.feathermind.matrix.wechat.mp;

import cn.hutool.core.util.URLUtil;
import com.feathermind.matrix.wechat.WxUserBinder;
import com.feathermind.matrix.wechat.config.WxProps;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信开放平台登录，暂时和mp共用配置，未来可以加一个mpServiceOpen
 */
@Slf4j
@Controller
@RequestMapping("/wechat/open")
public class WxOpenController {
    @Autowired
    WxMpService mpService;

    @Autowired(required = false)
    private WxUserBinder wxUserBinder;

    @GetMapping("/go")
    public String auth(HttpServletRequest req) {
        int serverPort = req.getServerPort();
        StringBuilder url = new StringBuilder();
        url.append(req.getScheme()).append("://").append(req.getServerName());

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append("/wechat/open/auth");

        return "redirect:" + mpService.buildQrConnectUrl(url.toString(), "snsapi_login", "0");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/auth")
    public String auth(@RequestParam("code") String code) {
        try {
            WxOAuth2AccessToken accessToken = mpService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo auth2UserInfo = mpService.getOAuth2Service().getUserInfo(accessToken, null);
            log.info("auth2UserInfo: {}", auth2UserInfo);
            wxUserBinder.bindUser(auth2UserInfo);
        } catch (Exception e) {
            log.error("获取用户信息异常！", e);
        }
        return "redirect:/index.html";
    }
}
