package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.feathermind.matrix.initializer.InitializeDomain

@Entity
@ToString(includePackage = false, includeNames = true, includes = 'name')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain
class ResDeptType {
    String code
    String name
    Integer seq

    Date dateCreated
    Date lastUpdated

    static mapping = {
        id name: 'code', generator: 'assigned'
        sort 'seq'
    }

    static directAgency = new ResDeptType(code: 'direct_agency', name: '区直属学校（单位）', seq: 10)
    static compulsory = new ResDeptType(code: 'compulsory', name: '九年制学校', seq: 20)
    static middleSchool = new ResDeptType(code: 'middle_school', name: '初级中学', seq: 30)
    static primarySchool = new ResDeptType(code: 'primary_school', name: '小学', seq: 40)
    static adultSchool = new ResDeptType(code: 'adult_school', name: '成人文化技术学校', seq: 40)
    static centralPreschool = new ResDeptType(code: 'central_preschool', name: '中心幼儿园', seq: 60)
    static publicPreschool = new ResDeptType(code: 'public_preschool', name: '公办幼儿园（非中心园）', seq: 70)
    static privatePreschool = new ResDeptType(code: 'private_preschool', name: '民办幼儿园', seq: 80)
    static privateSchool = new ResDeptType(code: 'private_school', name: '民办学校', seq: 90)
    static externalAgency = new ResDeptType(code: 'external_agency', name: '外部机构', seq: 99)
    static initList = [directAgency, compulsory, middleSchool, primarySchool, adultSchool,
                       centralPreschool, publicPreschool, privatePreschool, privateSchool, externalAgency]
}
