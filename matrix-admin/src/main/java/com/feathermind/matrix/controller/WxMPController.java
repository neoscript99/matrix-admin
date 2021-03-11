package com.feathermind.matrix.controller;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.feathermind.matrix.bean.*;
import com.feathermind.matrix.config.WxConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @see < a href="https://blog.csdn.net/qq_42851002/article/details/81327770">例1</ a>
 * @see < a href="https://learnku.com/articles/26718">例2</ a>
 * @see < a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">生成带参数的二维码</ a>
 * @see < a href="http://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo">个人测试公众号</ a>
 * @see < a href="https://mp.weixin.qq.com/debug/">公众平台接口调试</ a>
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({WxConfigProperties.class})
@RestController
@RequestMapping("/wechat")
public class WxMPController implements InitializingBean, DisposableBean {
    @Autowired
    private WechatBinder wechatBinder;
    private WxConfigProperties wxConfigProperties;
    private WxAccessToken wxAccessToken;

    /**
     * 第一步：获取access_token
     */
    public synchronized WxAccessToken getAccessToken() {
        if (wxAccessToken != null && wxAccessToken.isValid())
            return wxAccessToken;

        Map req = MapUtil.of(new String[][]{
                {"grant_type", "client_credential"},
                {"appid", wxConfigProperties.getAppId()},
                {"secret", wxConfigProperties.getAppSecret()}
        });
        String json = HttpUtil.get(wxConfigProperties.getAccessTokenUrl(), req);
        log.info(" 获取微信access_token结果：{}", json);
        wxAccessToken = JSONUtil.toBean(json, WxAccessToken.class);
        return wxAccessToken;
    }

    /**
     * 第二步：获取二维码地址
     */
    @PostMapping("qrcode")
    public WxQrcodeCreateRes createQrcode() {
        WxQrcodeCreateReq req = new WxQrcodeCreateReq();
        Map urlParam = Collections.singletonMap("access_token", getAccessToken().getAccess_token());
        //access_token拼到url中
        String url = HttpUtil.urlWithForm(wxConfigProperties.getQrcodeCreateUrl(), urlParam, StandardCharsets.UTF_8, false);

        String json = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(req)).execute().body();
        log.info("createQrcode res: {}", json);
        WxQrcodeCreateRes res = JSONUtil.toBean(json, WxQrcodeCreateRes.class);
        res.setScene_str(req.getAction_info().getScene().getScene_str());

        Map imgUrlParam = Collections.singletonMap("ticket", res.getTicket());
        res.setWxImgUrl(HttpUtil.urlWithForm(wxConfigProperties.getQrcodeShowUrl(), imgUrlParam, StandardCharsets.UTF_8, false));
        return res;
    }


    /**
     * 第三步：用户在前台扫码
     * 第四步：等待微信回调
     * https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
     */
    @GetMapping("callback")
    public String callbackGet(@RequestParam String echostr, HttpServletRequest httpRequest) {
        log.info("wechat callback: {}", httpRequest.getQueryString());
        //如果是微信校验，直接返回"随机字符串"
        return echostr;
    }

    @PostMapping("callback")
    public void callbackPost(@RequestBody SubscribeCallbackReq req, @RequestBody String body) {
        log.info("wechat callback: {}", body);
        String prefix = "qrscene_";
        String scene_str = req.getEventKey();
        if ("event".equals(req.getMsgType()) && "subscribe".equals(req.getEvent())
                && scene_str != null && scene_str.startsWith(prefix)) {
            userCache.put(scene_str.substring(prefix.length()), getUserInfo(req.getFromUserName()));
        }
    }

    /**
     * 第五步：前台发起检测，一般为定时多次发起
     */
    @PostMapping("checkLogin")
    public Map checkLogin(@RequestBody String scene_str) {
        WxUserInfo user = userCache.get(scene_str);
        return user != null ? wechatBinder.bindWechat(user) : Collections.singletonMap("success", false);
    }

    // 通过openid获取用户信息
    public WxUserInfo getUserInfo(String openid) {
        WxAccessToken wxAccessToken = getAccessToken();
        Map req = MapUtil.of(new String[][]{
                {"access_token", wxAccessToken.getAccess_token()},
                {"openid", openid}
        });
        String json = HttpUtil.get(wxConfigProperties.getUserInfoUrl(), req);
        return JSONUtil.toBean(json, WxUserInfo.class);
    }

    /**
     * 创建缓存，默认30分钟过期
     *
     * @see <a href="https://www.hutool.cn/docs/#/cache/TimedCache">文档</a>
     */
    private TimedCache<String, WxUserInfo> userCache = CacheUtil.newTimedCache(TimeUnit.MINUTES.toMillis(30));

    @Override
    public void afterPropertiesSet() throws Exception {
        //启动缓存定时清理任务，每小时一次
        userCache.schedulePrune(TimeUnit.HOURS.toMillis(1));
    }

    @Override
    public void destroy() throws Exception {
        userCache.cancelPruneSchedule();
    }

    @Autowired
    public void setWxConfigProperties(WxConfigProperties wxConfigProperties) {
        this.wxConfigProperties = wxConfigProperties;
    }
}
