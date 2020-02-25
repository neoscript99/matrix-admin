package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 专家每轮评分结果
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'achieveId,score')
@EqualsAndHashCode(includes = 'id')
class AchieveReviewScore {
    String id
    String achieveId
    ReviewRoundExpert roundExpert
    // 百分制
    Integer score

    Date dateCreated
    Date lastUpdated

    static mapping = {
        roundExpert fetch: 'join', lazy: false
    }
}
