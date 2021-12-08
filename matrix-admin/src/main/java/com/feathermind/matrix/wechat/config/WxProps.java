package com.feathermind.matrix.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "matrix.wechat")
public class WxProps {
    @NestedConfigurationProperty
    private WxMaProps miniapp;
    @NestedConfigurationProperty
    private WxMpProps mp;
}
