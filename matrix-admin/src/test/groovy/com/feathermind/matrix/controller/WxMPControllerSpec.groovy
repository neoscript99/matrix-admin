package com.feathermind.matrix.controller

import com.feathermind.matrix.config.WxConfigProperties
import groovy.util.logging.Slf4j
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@Slf4j
class WxMPControllerSpec extends Specification {
    private static WxMPController mpCtrl = new WxMPController();

    void setupSpec() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        def res = resourcePatternResolver.getResource('application.yml')

        def ps = new YamlPropertySourceLoader().load('main', res).get(0)

        WxConfigProperties wcp = new WxConfigProperties();
        wcp.setAppId(ps.getProperty('gorm.wechat.appId'))
        wcp.setAppSecret(ps.getProperty('gorm.wechat.appSecret'))
        log.info("WxConfigProperties: {}", wcp)
        mpCtrl.setWxConfigProperties(wcp)
    }

    def 'time unit test'() {
        given:
        def m = TimeUnit.HOURS.toMillis(1);
        log.info("Time: {}", m)
        expect:
        m == 1000 * 60 * 60
    }

    def 'wechat api test'() {
        given:
        def t = mpCtrl.createQrcode();
        log.info("Result: {}", t)
        expect:
        t.isValid()
    }
}
