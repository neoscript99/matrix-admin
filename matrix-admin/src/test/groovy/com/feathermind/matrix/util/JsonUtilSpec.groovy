package com.feathermind.matrix.util

import cn.hutool.core.bean.BeanUtil
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.feathermind.matrix.domain.sys.User
import groovy.transform.ToString
import org.springframework.beans.BeanUtils
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
        def map = [a: 'aa', b: 'bb', tdb: 888]
        def json = JsonUtil.toJson(new IgnoreTest(map))
        println(json)
        def backMap = JsonUtil.fromJson(json, Map)
        expect:
        !backMap.containsKey('a')
        !backMap.containsKey('b')
    }

    def 'domain equals'() {
        given:
        def u1 = new User(id: 'aaa', account: 'u1')
        def u2 = new User(id: 'aaa', account: 'u2')
        println(u1.equals(u2))
        expect:
        u1 == u2
    }

    def 'map to bean'() {
        given:
        def map = [a: 'aa', b: 'bb', ti: null, tdb: 888, nest: [nb1: 'aa']]
        def bean1 = JsonUtil.mapToBean(map, IgnoreTest)
        println(bean1)
        // 忽略@JsonIgnore配置
        println('BeanUtils.copyProperties bean2: ')
        def map2 = [nest: [nb2: 99]]
        def ignoreProps = BeanUtils.getPropertyDescriptors(IgnoreTest)*.name
        ignoreProps.removeAll(map2.keySet());
        println(ignoreProps)
        def bean2 = JsonUtil.mapToBean(map2, IgnoreTest);
        BeanUtils.copyProperties(bean2, bean1, ignoreProps.toArray(new String[0]))
        //BeanUtil.fillBeanWithMap(map2, bean1, true)
        println(bean1)
        expect:
        bean1.a == 'aa'
        bean1.nest.nb2 == 99
    }
}

@ToString(includeNames = true, includePackage = false)
class IgnoreTest implements TraitProps {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String a
    @JsonIgnore
    String b
    NestBean nest
}

@ToString(includeNames = true, includePackage = false)
class NestBean {
    String nb1
    Integer nb2
}

trait TraitProps {
    Integer ti
    String ts
    Date td
    Double tdb
    BigDecimal tbd
}
