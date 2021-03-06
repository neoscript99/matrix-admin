package com.feathermind.matrix.domain.wf

import com.feathermind.matrix.domain.sys.User
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 审核基本信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'apply,fromStatusCode,toStatusCode')
@EqualsAndHashCode(includes = 'id')
class ApplyLog {
    String id
    Apply apply
    User operator
    String fromStatusCode
    String toStatusCode
    String info

    Date dateCreated

    static mapping = {
        apply fetch: 'join', lazy: false
        operator fetch: 'join', lazy: false
        sort 'dateCreated'
    }

    static constraints = {
        info maxSize: 256, nullable: true
    }
}
