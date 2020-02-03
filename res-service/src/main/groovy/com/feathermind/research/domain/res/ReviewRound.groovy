package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果、论文评审轮次
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'plan,name')
@EqualsAndHashCode(includes = 'id')
class ReviewRound {
    String id
    ReviewPlan plan
    String name
    Integer seq
    BigDecimal passRate
    //本轮分数分段
    Integer classifyNumber = 2
    String beginDay
    String endDay
    //上级轮次，依赖上级结果
    ReviewRound parentRound

    Date dateCreated
    Date lastUpdated

    static mapping = {
        plan fetch: 'join', lazy: false
    }
}
