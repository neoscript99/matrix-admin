package org.yzedu.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
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
    YzDept dept
    YzUser personInCharge
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
    //成果拟形式
    String prepareAchieveFormCode
    String prepareFinishDay
    //课题状态
    String topicStatusCode

    //成果形式
    String achieveFormCode

    AttachmentInfo mainReport
    String finishDay
    //结题证书编号
    String topicCert

    Date dateCreated
    Date lastUpdated

    static mapping = {
        dept fetch: 'join', lazy: false
        personInCharge fetch: 'join', lazy: false
        initialReport fetch: 'join', lazy: false
        mainReport fetch: 'join', lazy: false
    }
    static constraints = {
        topicCode unique: true
        initialReport nullable: true
        mainReport nullable: true
        achieveFormCode nullable: true
        finishDay nullable: true
        topicCert nullable: true
    }
}
