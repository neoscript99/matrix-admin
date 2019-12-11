package org.yzedu.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果、论文评审计划
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'planName')
@EqualsAndHashCode(includes = 'id')
class ReviewPlan {
    String id
    //评审类型：成果或论文
    String reviewTypeCode
    String planName
    //年度
    Integer planYear
    String planBeginDay
    String planEndDay

    Integer rounds

    Date dateCreated
    Date lastUpdated
}
