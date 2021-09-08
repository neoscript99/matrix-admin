package com.feathermind.matrix.wechat.ma.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfig {
    @Autowired
    private WxMaProperties wxMaProperties;

    @Bean
    public WxMaService wxMaService() {
        if (this.wxMaProperties == null
                || StringUtils.isEmpty(this.wxMaProperties.getAppid())
                || StringUtils.isEmpty(this.wxMaProperties.getSecret())) {
            throw new RuntimeException("微信小程序相关配置错误，请检查！");
        }

        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        BeanUtils.copyProperties(wxMaProperties, config);

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);

        return service;
    }
}
