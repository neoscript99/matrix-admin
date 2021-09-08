package com.feathermind.matrix.controller

import cn.hutool.http.HttpUtil
import com.feathermind.matrix.util.JsonUtil
import com.feathermind.matrix.wechat.config.WxMpProperties
import com.feathermind.matrix.wechat.controller.WxMpController
import groovy.util.logging.Slf4j
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@Slf4j
class WxMpControllerSpec extends Specification {
    private static WxMpController mpCtrl = new WxMpController();
    private static final String SERVER_ROOT = 'http://localhost:8080'
    private static final String OPEN_ID = 'ofTwE6J5RKIMMNTd8Jf05ffiR2Kg'

    void setupSpec() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        def res = resourcePatternResolver.getResource('application.yml')

        def ps = new YamlPropertySourceLoader().load('main', res).get(0)

        WxMpProperties wcp = new WxMpProperties();
        wcp.setAppId(ps.getProperty('matrix.wechat.appId'))
        wcp.setAppSecret(ps.getProperty('matrix.wechat.appSecret'))
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

    def 'api test'() {
        given:
        def t = mpCtrl.getUserInfo(OPEN_ID);
        log.info("Result: {}", t)
        expect:
        t.city
    }

    def 'qrcode'() {
        given:
        def qrJson = HttpUtil.post("$SERVER_ROOT/wechat/qrcode", null);
        log.info("qrJson: {}", qrJson)
        def qrRes = JsonUtil.fromJson(qrJson, WxQrcodeCreateRes);
        expect:
        qrRes.valid
    }

    def 'callback get'() {
        given:
        def echostr = 'echostr';
        def t = HttpUtil.get("$SERVER_ROOT/wechat/callback", [echostr: echostr, a: 1, b: 'bbb']);
        log.info("Result: {}", t)
        expect:
        t == echostr
    }

    def 'callback post'() {
        given:
        def qrJson = HttpUtil.post("$SERVER_ROOT/wechat/qrcode", null);
        log.info("qrJson: {}", qrJson)
        def qrRes = JsonUtil.fromJson(qrJson, WxQrcodeCreateRes);
        def cbReq = """<xml>
  <ToUserName><![CDATA[toUser]]></ToUserName>
  <FromUserName><![CDATA[${OPEN_ID}]]></FromUserName>
  <CreateTime>123456789</CreateTime>
  <MsgType><![CDATA[event]]></MsgType>
  <Event><![CDATA[subscribe]]></Event>
  <EventKey><![CDATA[qrscene_${qrRes.scene_str}]]></EventKey>
  <Ticket><![CDATA[${qrRes.ticket}]]></Ticket>
</xml>"""
        def t = HttpUtil.post("$SERVER_ROOT/wechat/callback", cbReq.toString());
        log.info("Result: {}", t)

        def check = HttpUtil.createPost("$SERVER_ROOT/wechat/checkLogin")
                .contentType('application/json')
                .body(qrRes.scene_str)
                .execute().body();
        log.info("Result: {}", check)
        expect:
        qrRes.isValid()
    }

    def checkLogin(){
        def check = HttpUtil.createPost("$SERVER_ROOT/wechat/checkLogin")
                .contentType('application/json')
                .body('fe90fe83df33410892ead71d9b733b65')
                .execute().body();
        log.info("Result: {}", check)
        expect:
        check
    }
}
