package com.feathermind.matrix.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name')
@EqualsAndHashCode(includes = 'id')
class Dict {
    String id
    DictType type
    String code
    String name
    Integer seq
    Boolean enabled = true

    void setType(DictType type) {
        this.type = type
        if (code)
            id = Dict.genId(type, code)
    }

    void setCode(String code) {
        this.code = code
        if (type)
            id = Dict.genId(type, code)
    }

    static String genId(DictType type, String code) {
        return "$type.id~$code"
    }
    static mapping = {
        id generator: 'assigned', maxSize: 128
        type fetch: 'join', lazy: false
        version false
        sort 'seq'
    }
}
