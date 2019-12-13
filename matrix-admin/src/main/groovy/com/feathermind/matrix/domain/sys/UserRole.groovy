package com.feathermind.matrix.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.feathermind.matrix.initializer.InitializeDomain
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includes = 'user,role')
@InitializeDomain(depends = [User, Role])
@ToString(includes = ['role', 'user'])
@EqualsAndHashCode(includes = ['role', 'user'])
class UserRole implements Serializable {

    String id
    Role role
    User user

    static mapping = {
        role fetch: 'join', lazy: false
        user fetch: 'join', lazy: false
    }

    static constraints = {
        role(unique: 'user')
    }
    static graphql = true
    static initList = [
            (new UserRole(user: User.ADMIN, role: Role.ADMINISTRATORS)),
            (new UserRole(user: User.ANONYMOUS, role: Role.PUBLIC))]
}
