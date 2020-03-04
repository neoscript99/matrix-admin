package com.feathermind.research.domain.res

import com.fasterxml.jackson.annotation.JsonProperty
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 每个评分轮次的专家评分
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'achieveId,score')
@EqualsAndHashCode(includes = 'id')
class AchieveExpertScore {
    String id
    //到前台的数据会有一个achieveId属性，但不做achieve关联（topic的关联太多），后台如有使用自动lazy加载
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Achieve achieve
    ReviewRoundExpert roundExpert
    // 百分制
    Integer score

    Date dateCreated
    Date lastUpdated

    static mapping = {
        roundExpert fetch: 'join', lazy: false
    }

    static constraints = {
        achieve unique: 'roundExpert'
    }
}
