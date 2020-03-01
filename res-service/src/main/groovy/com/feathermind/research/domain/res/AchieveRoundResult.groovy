package com.feathermind.research.domain.res


import com.feathermind.matrix.trait.AutoTime
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 每个评分轮次，多专家的评分平均汇总
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'achieve,average')
@EqualsAndHashCode(includes = 'id')
class AchieveRoundResult implements AutoTime {
    String id
    //到前台的数据会有一个achieveId属性，但不做achieve关联（topic的关联太多），后台如有使用自动lazy加载
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Achieve achieve
    ReviewRound round
    BigDecimal average
    //打分明细：{name: 专家名 , score: 评分 }
    String scoresJson
    // 如果不是null，代表计算出错
    Boolean hasError
    String message

    static mapping = {
        achieve fetch: 'join', lazy: false
        round fetch: 'join', lazy: false
    }
    static constraints = {
        achieve unique: 'round'
        average nullable: true
        scoresJson nullable: true, maxSize: 1024
        message nullable: true
    }
}
