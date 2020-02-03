package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.domain.wf.Apply
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
    //说明：限300字，分三点简述：为什么做该课题（约20%字数），怎么在做（约50%字数），效果怎样（约30%字数）。
    AttachmentInfo summary
    //上传成果主报告盲评文本
    AttachmentInfo mainReport

    ReviewPlan reviewPlan
    Apply reviewApply
    CheckResult duplicateCheck

    static mapping = {
        personInCharge fetch: 'join', lazy: false
        mainReport fetch: 'join', lazy: false
        reviewPlan fetch: 'join', lazy: false
        reviewApply fetch: 'join', lazy: false
        duplicateCheck fetch: 'join', lazy: false
    }
    static constraints = {
        duplicateCheck nullable: true
    }
}
