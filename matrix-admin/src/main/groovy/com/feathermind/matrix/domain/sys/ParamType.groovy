package com.feathermind.matrix.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import com.feathermind.matrix.initializer.InitializeDomain

/**
 * 系统参数类型
 * @since Dec 23, 2010
 * @author wangchu
 */
@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name')
@EqualsAndHashCode(includes = 'code')
@InitializeDomain
class ParamType {

    static final ParamType SYSTEM = (new ParamType(code: 'system', name: '系统参数', seq: 1))
    String code
    String name
    Integer seq

    static mapping = {
        id name: 'code', generator: 'assigned'
        version false
        sort 'seq'
    }
    static final initList = [SYSTEM]
}
