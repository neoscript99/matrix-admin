package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果
 * “成果名称、负责人、团队成员”都可能和课题不一样
 * 团队成员
 * @see TopicAchieveMember
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'achieveName')
@EqualsAndHashCode(includes = 'id')
class TopicAchieve {
    String id
    //成果名称
    String achieveName
    //成果负责人姓名
    ResUser personInCharge
    //成果类别
    String achieveCateCode
    //立项情况
    String projectLevelCode
    //成果简述（内容提要）
    String summary
    //上传成果主报告盲评文本
    AttachmentInfo mainReport
    static mapping = {
        personInCharge fetch: 'join', lazy: false
        mainReport fetch: 'join', lazy: false
    }
    static constraints = {
        mainReport nullable: true
    }
}
