package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题基本信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'topicName')
@EqualsAndHashCode(includes = 'id')
class Topic {
    String id
    /*立项信息*/
    InitialPlan initialPlan
    //仅限申报重点课题填写
    String originTopicCode
    String originTopicName
    ResDept dept
    ResUser personInCharge
    /**申报编号，如2020年申报的，系统可以按所有类别课题申报提交先后设定为：2020001，2020002，等，类似于医院里的门诊号；
     * 立项编号：鄞州重点：YZZD2020001 鄞州规划:  YZGH2020001 鄞州艺术:  YZYS2020001,等等
     * 可将两个编号合并为一个课题编号
     * */
    String topicCode
    String topicName
    String topicCateCode
    String topicSourceCode
    String researchContentCode
    String researchSubjectCode
    String researchTargetCode
    //立项报告附件
    AttachmentInfo initialReport
    //成果拟形式，多选
    String prepareAchieveFormCodes
    String prepareFinishDay
    //课题状态
    String topicStatusCode
    Apply initialApply
    //立项编号，审批通过后生成，topicCateCode+year+001
    String initialCode

    /*结题信息*/
    //最后成果形式，多选
    String achieveFormCodes
    //主报告
    AttachmentInfo mainReport
    String finishDay
    //结题证书编号
    //灰色，审核者填写；先不显示，可以根据从立项编号生成
    String topicCert
    Apply finishApply


    /*评比信息*/
    ReviewPlan reviewPlan
    TopicAchieve topicAchieve
    Apply reviewApply
    CheckResult duplicateCheck

    Date dateCreated
    Date lastUpdated

    static mapping = {
        initialPlan fetch: 'join', lazy: false
        dept fetch: 'join', lazy: false
        personInCharge fetch: 'join', lazy: false
        initialReport fetch: 'join', lazy: false, cascade: 'delete'
        initialApply fetch: 'join', lazy: false, cascade: 'delete'

        mainReport fetch: 'join', lazy: false, cascade: 'delete'
        finishApply fetch: 'join', lazy: false

        reviewPlan fetch: 'join', lazy: false
        reviewApply fetch: 'join', lazy: false
        topicAchieve fetch: 'join', lazy: false
        duplicateCheck fetch: 'join', lazy: false
    }
    static constraints = {
        originTopicCode nullable: true
        originTopicName nullable: true
        initialReport nullable: true
        prepareAchieveFormCodes maxSize: 256
        initialCode nullable: true

        mainReport nullable: true
        achieveFormCodes nullable: true, maxSize: 256
        finishDay nullable: true
        topicCert nullable: true
        finishApply nullable: true

        reviewPlan nullable: true
        topicAchieve nullable: true
        reviewApply nullable: true
        duplicateCheck nullable: true
    }
}
