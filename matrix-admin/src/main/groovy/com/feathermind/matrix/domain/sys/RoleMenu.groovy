package com.feathermind.matrix.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includes = 'role,menu')
@ToString(includes = ['role', 'menu'])
@EqualsAndHashCode(includes = 'id')
class RoleMenu implements Serializable {

    String id
    Role role
    Menu menu

    static mapping = {
        role fetch: 'join', lazy: false
        menu fetch: 'join', lazy: false
    }

    static constraints = {
        menu(unique: 'role')
    }
}
