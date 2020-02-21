package com.feathermind.research.domain.res

import com.feathermind.matrix.util.DateUtil
import grails.gorm.annotation.Entity

import com.feathermind.matrix.initializer.InitializeDomain
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题立项申报计划
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'planName')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = 'dev')
class InitialPlan implements PlanTrait {
    String id
    String planName
    //立项年度
    Integer planYear
    //计划类型，相当于课题大类
    String planCateCode
    //申报开始日期
    String planBeginDay
    //申报截止日期
    String planEndDay
    //结题截止日期
    String finishDeadline
    //每个单位最多可申报数
    Integer maxNumberPerDept

    static constraints = {
        maxNumberPerDept nullable: true
    }
    static DemoPlan = new InitialPlan([planName      : '2020年教育科学规划课题申报计划',
                                       planYear      : 2020, planCateCode: 'YZGH',
                                       planBeginDay  : DateUtil.dayStr(-10), planEndDay: DateUtil.dayStr(10),
                                       finishDeadline: DateUtil.dayStr(730)])
    static DemoPlan2 = new InitialPlan([planName      : '2020年教育科学重点课题申报计划',
                                        planYear      : 2020, planCateCode: 'YZZD',
                                        planBeginDay  : DateUtil.dayStr(-30), planEndDay: DateUtil.dayStr(30),
                                        finishDeadline: DateUtil.dayStr(666)])
    static DemoPlan3 = new InitialPlan([planName      : '2020年教育科学幼教课题申报计划',
                                        planYear      : 2020, planCateCode: 'YZYJ',
                                        planBeginDay  : DateUtil.dayStr(-1), planEndDay: DateUtil.dayStr(90),
                                        finishDeadline: DateUtil.dayStr(555), maxNumberPerDept: 1])
    static initList = [DemoPlan, DemoPlan2, DemoPlan3]
}
