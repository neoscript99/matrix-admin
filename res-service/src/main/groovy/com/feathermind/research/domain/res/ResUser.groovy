package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.User
import grails.gorm.annotation.Entity

/**
 * 鄞州教育局人员信息
 * todo 用户成为课题成员后，单位管理员不能再进行基本信息修改
 *
 * 课题负责人作为单位人员，account不需要通过规则生成
 * 评审专家作为外部人员
 * 都维护到这张表
 */
@Entity
class ResUser extends User{
    ResDept dept
    /**
     * 课题负责人姓名	性别（上级存在）	身份证号码	出生日期	职务职称	专业	最后学历▼
     */
    String idCard
    String birthDay
    //职务职称
    String title
    String major
    String degreeCode

    static constraints = {
        idCard nullable: true
        birthDay nullable: true
        title nullable: true
        major nullable: true
        degreeCode nullable: true
    }
}
