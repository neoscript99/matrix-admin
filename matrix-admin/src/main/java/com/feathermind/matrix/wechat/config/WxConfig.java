package com.feathermind.matrix.wechat.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WxProps.class)
@Slf4j
public class WxConfig {
    @Autowired
    private WxProps matrixWxProps;

    @Bean
    public WxMaService wxMaService() {
        WxMaProps wxMaProperties = matrixWxProps.getMiniapp();
        if (wxMaProperties == null
                || StringUtils.isEmpty(wxMaProperties.getAppid())
                || StringUtils.isEmpty(wxMaProperties.getSecret())) {
            log.warn("没有微信小程序相关配置错误，不启动服务！");
            return null;
        }

        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        BeanUtils.copyProperties(wxMaProperties, config);

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);

        return service;
    }
}
