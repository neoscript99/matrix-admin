package com.feathermind.matrix.util

import cn.hutool.core.lang.UUID
import cn.hutool.http.HttpUtil
import groovy.util.logging.Slf4j
import spock.lang.Specification

import java.nio.charset.StandardCharsets

@Slf4j
class HutoolUtilSpec extends Specification {

    def 'uuid test'() {
        given:
        log.info(UUID.fastUUID().toString())
        log.info(UUID.fastUUID().toString(true))

        expect:
        true;
    }

    def 'url With Form'() {
        given:
        def utf8 = StandardCharsets.UTF_8;
        def url = 'https://api.weixin.qq.com/cgi-bin/qrcode/create'
        Map urlParam = [ticket: '43_CYJdGaX08Ykpu0V6Lqj-{}PlAudAcqfHXWfAJAQGF', a: 1];
        //utf8本身已编码，isEncodeParams对键和值做转义处理
        log.info(HttpUtil.urlWithForm(url, urlParam, utf8, false))
        log.info(HttpUtil.urlWithForm(url, urlParam, utf8, true))
        def paramStr = HttpUtil.toParams(urlParam);
        log.info(paramStr)
        log.info(HttpUtil.decodeParams(paramStr, utf8))

        expect:
        true;
    }
}
