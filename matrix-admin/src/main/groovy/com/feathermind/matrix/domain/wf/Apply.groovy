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
    //增加流程所有人信息，支持双向关联
    String ownerId
    //可用于流程类型设置，各系统可自行处理
    String ownerType
    String ownerName

    Date dateCreated
    Date lastUpdated

    static mapping = {
        applier fetch: 'join', lazy: false
    }
    static constraints = {
        passTime nullable: true
        name maxSize: 50
        ownerId maxSize: 80, nullable: true
        ownerType nullable: true
        //超过长度的应该自行截断
        ownerName maxSize: 128, nullable: true
    }
}
