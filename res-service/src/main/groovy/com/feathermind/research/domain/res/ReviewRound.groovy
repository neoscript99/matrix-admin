package com.feathermind.research.domain.res

import com.feathermind.matrix.initializer.InitializeDomain
import com.feathermind.matrix.util.DateUtil
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果、论文评比轮次
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'plan,name')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = 'dev', depends = ReviewPlan)
class ReviewRound {
    String id
    ReviewPlan plan
    String name
    //本轮等级数，暂不分等级，数据导出后可自由设置
    //Integer grades
    //ignore-max-min
    String avgAlgorithmCode
    //暂时不需要开始日期，新建后直接算开始
    //String beginDay
    String endDay
    String expertPassword = 'abc000'
    //上级轮次，依赖上级结果
    ReviewRound parentRound
    //通过比例
    BigDecimal passRate
    //https://ant.design/components/badge-cn/
    //success error default processing warning
    String runStatus = 'default'
    String runError

    Date dateCreated
    Date lastUpdated

    static mapping = {
        plan fetch: 'join', lazy: false
        parentRound fetch: 'join', lazy: false
        sort 'dateCreated'
    }

    static constraints = {
        parentRound nullable: true
        passRate nullable: true
        runError nullable: true, maxSize: 1024
    }
    static DemoRound1 =
            new ReviewRound(plan: ReviewPlan.DemoPlan1, name: '第一轮',
                    avgAlgorithmCode: 'ignore-max-min',
                    endDay: DateUtil.dayStr(10))
    static DemoPaperRound =
            new ReviewRound(plan: ReviewPlan.DemoPaperPlan, name: '第一轮',
                    avgAlgorithmCode: 'normal',
                    endDay: DateUtil.dayStr(210))
    static initList = [DemoRound1, DemoPaperRound]
}
