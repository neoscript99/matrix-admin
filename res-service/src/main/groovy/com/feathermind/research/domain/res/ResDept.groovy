package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.Department
import grails.gorm.annotation.Entity
import groovy.transform.ToString

/**
 * 鄞州教育局单位信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'name,contact')
class ResDept extends Department{

    //联系人
    String contact
    String telephone
    String cellphone
    String shortDial
    String email
    String address

    //每个立项计划单位默认申报数
    Integer defaultApplyNum = 0

    Date dateCreated
    Date lastUpdated

    static mapping = {
    }
    static constraints = {
        contact nullable: true
        telephone nullable: true
        cellphone nullable: true
        shortDial nullable: true
        email nullable: true
        address nullable: true
    }
}
