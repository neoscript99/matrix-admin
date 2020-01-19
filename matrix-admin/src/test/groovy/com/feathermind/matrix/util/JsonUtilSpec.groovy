package com.feathermind.matrix.util

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.feathermind.matrix.domain.sys.Param
import spock.lang.Specification


class JsonUtilSpec extends Specification {
    def "empty String to Json"() {
        given:
        println JsonUtil.fromJson('null', Map, false)
        println JsonUtil.fromJson('{}', Map, false)
        expect:
        true
    }

    def 'ignore json'() {
        given:
        println(JsonUtil.toJson(new IgnoreTest(a: 'aa', b: 'bb')))
        expect:
        true
    }
}

@JsonIgnoreProperties(value = ['password', "b"], allowSetters = true)
class IgnoreTest {
    String a
    String b
}
