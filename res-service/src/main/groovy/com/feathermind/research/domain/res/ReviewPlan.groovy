package com.feathermind.research.domain.res

import com.feathermind.matrix.initializer.InitializeDomain
import com.feathermind.matrix.util.DateUtil
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果、论文评比计划
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'planName')
@InitializeDomain(profiles = 'dev')
@EqualsAndHashCode(includes = 'id')
class ReviewPlan implements PlanTrait {
    String id
    //评比类型：成果或论文
    String reviewTypeCode

    static DemoPlan1 = new ReviewPlan([planName    : '2020年教育科学幼教课题评比计划',
                                       planYear    : 2020, reviewTypeCode: 'topic',
                                       planBeginDay: DateUtil.dayStr(-10), planEndDay: DateUtil.dayStr(90)])
    static DemoPlan2 = new ReviewPlan([planName    : '2020年教育科学论文评比计划',
                                       planYear    : 2020, reviewTypeCode: 'paper',
                                       planBeginDay: DateUtil.dayStr(-5), planEndDay: DateUtil.dayStr(110)])
    static initList = [DemoPlan1, DemoPlan2]
}
