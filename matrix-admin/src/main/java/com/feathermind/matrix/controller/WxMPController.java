package com.feathermind.matrix.controller;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.feathermind.matrix.bean.WxAccessToken;
import com.feathermind.matrix.bean.WxUserInfo;
import com.feathermind.matrix.config.WxConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @see < a href=" ">例1</ a>
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

    @GetMapping("qrcode/{scene_str}")
    public ResponseEntity<byte[]> qrcode(@PathVariable("scene_str") String scene_str) {
        return ResponseEntity.notFound().build();
    }

    /**
     * https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
     *
     * @return
     */
    @GetMapping("callback")
    @ResponseBody
    public Map callback(@RequestBody Map req) {
        //如果是微信校验，直接返回"随机字符串"
        if (req.get("signature") != null && req.get("echostr") != null)
            return Collections.singletonMap("echostr", req.get("echostr"));
        return null;
    }

    // 检测登录
    @PostMapping("checkLogin")
    @ResponseBody
    public Map<String, Object> checkLogin(@PathVariable("scene") String scene) {
        // 根据scene_str查询数据库，获取对应记录
        // SELECT * FROM wechat_user_info WHERE event_key='scene_str';
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (true) {
            returnMap.put("result", "true");
        } else {
            returnMap.put("result", "false");
        }
        return returnMap;
    }

    // 通过openid获取用户信息
    public WxUserInfo getUserInfoByOpenid(String openid) {
        WxAccessToken wxAccessToken = getAccessToken();
        Map req = MapUtil.of(new String[][]{
                {"access_token", wxAccessToken.getAccess_token()},
                {"openid", openid}
        });
        String json = HttpUtil.get(wxConfigProperties.getUserInfoUrl(), req);
        return JSONUtil.toBean(json, WxUserInfo.class);
    }

    /**
     * 创建缓存，默认4毫秒过期
     *
     * @see <a href="https://www.hutool.cn/docs/#/cache/TimedCache">文档</a>
     */
    private TimedCache<String, String> timedCache = CacheUtil.newTimedCache(4);

    @Override
    public void afterPropertiesSet() throws Exception {
        //启动缓存定时清理任务，每小时一次
        timedCache.schedulePrune(TimeUnit.HOURS.toMillis(1));
    }

    @Override
    public void destroy() throws Exception {
        timedCache.cancelPruneSchedule();
    }

    @Autowired
    public void setWxConfigProperties(WxConfigProperties wxConfigProperties) {
        this.wxConfigProperties = wxConfigProperties;
    }
}
