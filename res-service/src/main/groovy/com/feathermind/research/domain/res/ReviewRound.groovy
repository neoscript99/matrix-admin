package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果、论文评比轮次
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'plan,name')
@EqualsAndHashCode(includes = 'id')
class ReviewRound {
    String id
    ReviewPlan plan
    String name
    //本轮等级数
    Integer grades
    String avgAlgorithmCode
    String beginDay
    String endDay
    //上级轮次，依赖上级结果
    ReviewRound parentRound
    //通过等级数，设置上轮的情况下需要设置
    BigDecimal passGrades

    Date dateCreated
    Date lastUpdated

    static mapping = {
        plan fetch: 'join', lazy: false
        parentRound fetch: 'join', lazy: false
        sort 'dateCreated'
    }

    static constraints = {
        parentRound nullable: true
        passGrades nullable: true
    }
}
