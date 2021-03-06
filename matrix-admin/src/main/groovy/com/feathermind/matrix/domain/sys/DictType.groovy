package com.feathermind.matrix.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name')
@EqualsAndHashCode(includes = 'id')
class DictType {
    String id
    String name
    //上级字典类别项，这样可以支持多级字典
    String parentId

    static mapping = {
        id generator: 'assigned'
        version false
        sort 'seq'
    }
    static constraints = {
        parentId nullable: true
    }
}
