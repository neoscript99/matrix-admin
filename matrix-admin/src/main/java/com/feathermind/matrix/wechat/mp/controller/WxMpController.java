package com.feathermind.matrix.wechat.mp.controller;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.feathermind.matrix.wechat.WxBinder;
import com.feathermind.matrix.wechat.config.WxProps;
import com.feathermind.matrix.wechat.mp.bean.WxQrcodeCreateReq;
import com.feathermind.matrix.wechat.config.WxMpProps;
import lombok.extern.slf4j.Slf4j;
import com.feathermind.matrix.wechat.mp.bean.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 微信公众号-服务号扫码（关注+登录）
 * 注意：订阅号不支持生成带参数的二维码
 *
 * @see < a href="https://blog.csdn.net/qq_42851002/article/details/81327770">例1</ a>
 * @see < a href="https://learnku.com/articles/26718">例2</ a>
 * @see < a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">生成带参数的二维码</ a>
 * @see < a href="http://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo">个人测试公众号</ a>
 * @see < a href="https://mp.weixin.qq.com/debug/">公众平台接口调试</ a>
 */
@Slf4j
@RestController
@RequestMapping("/wechat/mp")
public class WxMpController implements InitializingBean, DisposableBean {
    @Autowired(required = false)
    private WxBinder wxBinder;

    @Autowired
    private WxProps matrixWxProps;
    private WxAccessToken wxAccessToken;

    /**
     * 第一步：获取access_token
     */
    public synchronized WxAccessToken getAccessToken() {
        if (wxAccessToken != null && wxAccessToken.isValid())
            return wxAccessToken;
        WxMpProps wxMpProps = matrixWxProps.getMp();
        Map req = MapUtil.of(new String[][]{
                {"grant_type", "client_credential"},
                {"appid", wxMpProps.getAppId()},
                {"secret", wxMpProps.getAppSecret()}
        });
        String json = HttpUtil.get(wxMpProps.getAccessTokenUrl(), req);
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
        WxAccessToken token = getAccessToken();
        if (!token.isValid()) {
            WxQrcodeCreateRes errorRes = new WxQrcodeCreateRes();
            errorRes.setError(token.getErrmsg());
            return errorRes;
        }
        Map urlParam = Collections.singletonMap("access_token", token.getAccess_token());
        WxMpProps wxMpProps = matrixWxProps.getMp();
        //access_token拼到url中
        String url = HttpUtil.urlWithForm(wxMpProps.getQrcodeCreateUrl(), urlParam, StandardCharsets.UTF_8, false);

        String json = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(req)).execute().body();
        log.info("createQrcode res: {}", json);
        WxQrcodeCreateRes res = JSONUtil.toBean(json, WxQrcodeCreateRes.class);
        res.setScene_str(req.getAction_info().getScene().getScene_str());

        Map imgUrlParam = Collections.singletonMap("ticket", res.getTicket());
        res.setWxImgUrl(HttpUtil.urlWithForm(wxMpProps.getQrcodeShowUrl(), imgUrlParam, StandardCharsets.UTF_8, false));
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

    /**
     * 第五步：接收微信回调
     * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html
     */
    @PostMapping("callback")
    public void callbackPost(@RequestBody WxCallbackReq req) {
        log.info("wechat callback: {}", req);
        String scene_str = req.EventKey;
        //todo Event可能有subscribe和scan两种，prefix可能没有，需再测试
        if ("event".equals(req.MsgType) && scene_str != null
                && ("SCAN".equals(req.Event) || "subscribe".equals(req.Event))) {
            //首次关注的事件类型为subscribe，eventKey有前缀
            String prefix = "qrscene_";
            if ("subscribe".equals(req.Event) && scene_str.startsWith(prefix)) {
                scene_str = scene_str.substring(prefix.length());
            }
            openidCache.put(scene_str, req.FromUserName);
            log.info("wechat user cache size: {}", openidCache.size());
        }
    }

    /**
     * 第六步：通过openid获取用户信息
     */
    public WxUserInfo getUserInfo(String openid) {
        log.debug("getUserInfo req: {}", openid);
        WxAccessToken wxAccessToken = getAccessToken();
        Map req = MapUtil.of(new String[][]{
                {"access_token", wxAccessToken.getAccess_token()},
                {"openid", openid}
        });
        WxMpProps wxMpProps = matrixWxProps.getMp();
        String json = HttpUtil.get(wxMpProps.getUserInfoUrl(), req);
        log.debug("getUserInfo res: {}", json);
        return JSONUtil.toBean(json, WxUserInfo.class);
    }

    /**
     * 第七步：前台发起检测，一般为定时多次发起
     * 如果微信扫码成功
     * - 如果wechatBinder bean存在，进行绑定处理，建议返回map包含：{"success": true}
     * - 如果wechatBinder bean不存在，返回{"success": true, "user": 微信对应的用户信息}
     * 否则
     * - 返回json：{"success": false}
     */
    @PostMapping(value = "checkBind")
    public Object checkBind(@RequestBody WxBindReq req) {
        //log.debug("checkLogin: {}", scene_str);
        String openid = openidCache.get(req.getScene_str());
        WxUserInfo user = StringUtils.hasText(openid) ? getUserInfo(openid) : null;
        return wxBinder.bindWxMpUser(user);
    }

    /**
     * 根据二维码对应的scene_str，对登录成功的用户进行缓存，1分钟过期
     *
     * @see <a href="https://www.hutool.cn/docs/#/cache/TimedCache">文档</a>
     */
    private TimedCache<String, String> openidCache = CacheUtil.newTimedCache(TimeUnit.MINUTES.toMillis(1));

    @Override
    public void afterPropertiesSet() {
        //启动缓存定时清理任务，每10分钟一次
        openidCache.schedulePrune(TimeUnit.MINUTES.toMillis(10));
    }

    @Override
    public void destroy() {
        openidCache.cancelPruneSchedule();
    }
}
