package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.feathermind.matrix.initializer.InitializeDomain

@Entity
@ToString(includePackage = false, includeNames = true, includes = 'name')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = 'dev')
class ResDeptType {
    String code
    String name

    Date dateCreated
    Date lastUpdated

    static mapping = {
        id name: 'code', generator: 'assigned'
    }

    static initList = [
            new ResDeptType(code: 'manage_office',name: '区直属学校（单位）'),
            new ResDeptType(code: 'compulsory',name: '九年制学校'),
            new ResDeptType(code: 'middle_school',name: '初级中学'),
            new ResDeptType(code: 'primary_school',name: '小学'),
            new ResDeptType(code: 'adult_school',name: '成人文化技术学校'),
            new ResDeptType(code: 'central_preschool',name: '中心幼儿园'),
            new ResDeptType(code: 'central_preschool',name: '公办幼儿园（非中心园）'),
            new ResDeptType(code: 'central_preschool',name: '民办幼儿园'),
            new ResDeptType(code: 'central_preschool',name: '民办学校')]
}
