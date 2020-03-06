package com.feathermind.matrix.util

import spock.lang.Specification

class StringUtilSpec extends Specification {
    def 'GString equal test'() {
        given:
        def name = 'name'
        List<String> list = ['aa', 'bb', "${name}Test"]

        expect:
        !list.contains('nameTest')
        list.find { it.compareTo('nameTest') == 0 }
    }

    def 'list print'() {
        given:
        def name = 'User'
        def authorities = [['SysRead', "${name}All", "${name}Read"], "${name}ReadOne"]
        println "当前用户无以下权限：${authorities.flatten()}"
        mm(null, null)
        expect:
        true;
    }

    static mm(Object... os) {
        println os
    }
}
