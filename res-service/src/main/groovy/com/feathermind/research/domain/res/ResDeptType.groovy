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
    //大类
    String cateName
    //大类编码
    String cateCode

    Date dateCreated
    Date lastUpdated

    static mapping = {
        id name: 'code', generator: 'assigned'
        sort 'seq'
    }

    static directAgency = new ResDeptType(code: 'direct_agency', name: '区直属学校（单位）', cateName: '直属', cateCode: 'ZS', seq: 10)
    static compulsory = new ResDeptType(code: 'compulsory', name: '九年制学校', cateName: '九年制', cateCode: 'JN', seq: 20)
    static middleSchool = new ResDeptType(code: 'middle_school', name: '初级中学', cateName: '初中', cateCode: 'CZ', seq: 30)
    static primarySchool = new ResDeptType(code: 'primary_school', name: '小学', cateName: '小学', cateCode: 'XX', seq: 40)
    static adultSchool = new ResDeptType(code: 'adult_school', name: '成人文化技术学校', cateName: '成教', cateCode: 'CJ', seq: 50)
    static centralPreschool = new ResDeptType(code: 'central_preschool', name: '中心幼儿园', cateName: '幼教', cateCode: 'YJ', seq: 60)
    static publicPreschool = new ResDeptType(code: 'public_preschool', name: '公办幼儿园（非中心园）', cateName: '幼教', cateCode: 'YJ', seq: 70)
    static privatePreschool = new ResDeptType(code: 'private_preschool', name: '民办幼儿园', cateName: '幼教', cateCode: 'YJ', seq: 80)
    static privateSchool = new ResDeptType(code: 'private_school', name: '民办学校', cateName: '民办', cateCode: 'MB', seq: 90)
    static manageCenter = new ResDeptType(code: 'manage_center', name: '城区教育管理服务中心', cateName: '教育管理', cateCode: 'GL', seq: 100)
    static researchCounsel = new ResDeptType(code: 'research_counsel', name: '镇（街道）教育研究辅导室', cateName: '教辅', cateCode: 'JF', seq: 110)
    static externalAgency = new ResDeptType(code: 'external_agency', name: '外部机构', cateName: '外部', cateCode: 'WB', seq: 999)
    static initList = [directAgency, compulsory, middleSchool, primarySchool, adultSchool,
                       centralPreschool, publicPreschool, privatePreschool, privateSchool,
                       manageCenter, researchCounsel, externalAgency]
}
