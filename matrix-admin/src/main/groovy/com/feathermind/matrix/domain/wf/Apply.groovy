package com.feathermind.matrix.domain.wf

import com.feathermind.matrix.domain.sys.User
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 流程审批基本信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'name')
@EqualsAndHashCode(includes = 'id')
class Apply {
    String id
    String name
    String type
    User applier
    String statusCode
    Date passTime

    Date dateCreated
    Date lastUpdated

    static mapping = {
        applier fetch: 'join', lazy: false
    }
    static constraints = {
        passTime nullable: true
        name maxSize: 50
    }
}
