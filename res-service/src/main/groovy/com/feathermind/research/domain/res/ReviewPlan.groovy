package com.feathermind.research.domain.res

import com.feathermind.matrix.initializer.InitializeDomain
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果、论文评比计划
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'planName')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = 'dev')
class ReviewPlan implements PlanTrait {
    String id
    //评比类型：成果或论文
    String reviewTypeCode
    String planName
    //年度
    Integer planYear
    String planBeginDay
    String planEndDay

    Date dateCreated
    Date lastUpdated

    static DemoPlan1 = new ReviewPlan([planName    : '2020年教育科学幼教课题评比计划',
                                       planYear    : 2020, reviewTypeCode: 'topic',
                                       planBeginDay: '2020-02-01', planEndDay: '2020-05-10'])
    static DemoPlan2 = new ReviewPlan([planName    : '2020年教育科学论文评比计划',
                                       planYear    : 2020, reviewTypeCode: 'paper',
                                       planBeginDay: '2020-02-01', planEndDay: '2020-04-10'])
    static initList = [DemoPlan1, DemoPlan2]
}
