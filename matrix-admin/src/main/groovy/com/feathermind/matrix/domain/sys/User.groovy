package com.feathermind.matrix.domain.sys

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.feathermind.matrix.util.EncoderUtil
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import com.feathermind.matrix.initializer.InitializeDomain

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name,dept')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(depends = [Department])
/**
 * 管理后台的系统用户信息
 * sys包下都是管理后台相关domain
 */
class User {
    static encodePassword(String password) {
        return EncoderUtil.sha256(password)
    }

    String id
    String account
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password = encodePassword('anonymous')
    String name
    Boolean editable = true
    Boolean enabled = true

    Department dept

    String cellPhone
    String email
    String sexCode

    Date lastUpdated;
    Date dateCreated;

    static mapping = {
        dept fetch: 'join', lazy: false
    }
    static constraints = {
        account unique: true
        password maxSize: 80
        cellPhone nullable: true
        email nullable: true
        sexCode nullable: true
    }


    static final User ADMIN = (new User(account: 'admin', name: '系统管理员', dept: Department.HEAD_OFFICE,
            editable: false))
    static final User ANONYMOUS = (new User(account: 'anonymous', name: '匿名帐号', dept: Department.HEAD_OFFICE,
            editable: false))
    static final User TEST_USER = (new User(account: 'test.user', name: '测试用户', dept: Department.HEAD_OFFICE,
            editable: true))

    static final initList = [ADMIN, ANONYMOUS, TEST_USER]
}
