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
    // 专家排序
    Integer seq

    Date dateCreated
    Date lastUpdated

    static mapping = {
        expert fetch: 'join', lazy: false
        round fetch: 'join', lazy: false
        sort 'seq'
    }
}
