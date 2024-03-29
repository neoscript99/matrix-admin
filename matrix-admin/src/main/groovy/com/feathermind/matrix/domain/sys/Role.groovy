package com.feathermind.matrix.domain.sys

import com.feathermind.matrix.trait.AutoTime
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import com.feathermind.matrix.initializer.InitializeDomain

/**
 * 创建时间 Dec 14, 2010
 * @author wangchu
 */
@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'roleName')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain
class Role implements AutoTime {
    //管理员，所有菜单权限
    static final Role ADMINISTRATORS = new Role(roleName: '系统管理员', roleCode: 'Administrators',
            editable: false, description: '系统参数管理.', authorities: 'SysAdmin')

    static final Role NORMAL_USERS = new Role(roleName: '普通用户', roleCode: 'NormalUsers',
            editable: false, description: '普通用户.', authorities: 'UserWriteOne,FileDownload')

    //公开用户，可支持匿名用户查看网站通知、下载附件等开放功能
    static final Role PUBLIC = new Role(roleName: '默认角色', roleCode: 'Public',
            editable: false, description: '默认角色可开放给CAS登录用户和匿名用户.', authorities: 'FileDownload')

    String id
    String roleName
    String roleCode
    String description
    Boolean editable = true
    Boolean enabled = true

    String authorities

    static mapping = {
    }
    static constraints = {
        description nullable: true, blank: true, maxSize: 128
        authorities nullable: true, blank: true, maxSize: 512
        roleCode unique: true
    }

    static initList = [ADMINISTRATORS, NORMAL_USERS, PUBLIC]
    static graphql = true
}
