package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 评分轮次、专家关联表
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'round,expert')
@EqualsAndHashCode(includes = 'id')
class ReviewRoundExpert {
    String id
    ReviewRound round
    ResUser expert
    Integer seq
    //可以设置专家评分权重
    //BigDecimal weight
    // 总数
    Integer totalNumber
    // 已评分数量
    Integer scoredNumber
    // 平均分
    BigDecimal average

    Date dateCreated
    Date lastUpdated

    static mapping = {
        expert fetch: 'join', lazy: false
        round fetch: 'join', lazy: false
        sort 'seq'
    }

    static constraints = {
        totalNumber nullable: true
        scoredNumber nullable: true
        average nullable: true
    }
}
