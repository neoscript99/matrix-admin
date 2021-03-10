package com.feathermind.matrix.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.feathermind.matrix.config.MatrixWxmpConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @see https://blog.csdn.net/qq_42851002/article/details/81327770
 * @see https://learnku.com/articles/26718
 */
@Configuration
@EnableConfigurationProperties({MatrixWxmpConfigProperties.class})
@RestController
@RequestMapping("/wechat")
public class WechatController {
    @Autowired
    MatrixWxmpConfigProperties wxmpConfigProperties;

    @GetMapping("qrcode/{scene}")
    public ResponseEntity<byte[]> qrcode(@PathVariable("id") String id) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("callback")
    public ResponseEntity<byte[]> callback(@PathVariable("id") String id) {
        return ResponseEntity.notFound().build();
    }

    // 检测登录
    @PostMapping("checkLogin")
    public @ResponseBody
    Map<String, Object> checkLogin(@PathVariable("scene") String scene) {
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

    private String getAccessToken() throws Exception {
        Map req = MapUtil.of(new String[][]{
                {"grant_type", "client_credential"},
                {"appid", wxmpConfigProperties.getAppId()},
                {"secret", wxmpConfigProperties.getAppSecret()}
        });
        Map res = JSONUtil.toBean(HttpUtil.get(wxmpConfigProperties.getAccessTokenUrl(), req), Map.class);
        System.out.println(res);
        return res.get("access_token").toString();
    }
}
