package com.feathermind.research.domain.res

import com.fasterxml.jackson.annotation.JsonIgnore
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 每个评分轮次，多专家的评分平均汇总
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'achieve,average')
@EqualsAndHashCode(includes = 'id')
class AchieveRoundResult {
    String id
    //到前台的数据会有一个achieveId属性，但不做achieve关联（topic的关联太多），后台如有使用自动lazy加载
    @JsonIgnore
    Achieve achieve
    ReviewRound round
    BigDecimal average
    String expertScores
    // 如果不是null，代表计算出错
    Boolean hasError
    String error

    Date dateCreated
    Date lastUpdated

    static mapping = {
        round fetch: 'join', lazy: false
    }
    static constraints = {
        average nullable: true
        grade nullable: true
        error nullable: true
    }
}
