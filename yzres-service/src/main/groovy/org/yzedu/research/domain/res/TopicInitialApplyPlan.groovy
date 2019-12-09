package org.yzedu.research.domain.res

import grails.gorm.annotation.Entity

import com.feathermind.matrix.initializer.InitializeDomain
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * 课题立项申报计划
 */
@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includeNames = true, includes = 'name')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = 'dev')
class TopicInitialApplyPlan {
    String id
    String name
    //立项年度
    String year
    //课题类别 字典id
    String topicCateId
    //申报开始日期
    String applyBeginDay
    //申报截止日期
    String applyEndDay
    //结题截止日期
    String tieDeadline

    //单位申报数比例设置
    String deptNumberRate
    //单位申报数，比例计算取整方式：1、上浮Ciel 2、四舍五入normal 3、
    String roundMethod

    Date dateCreated
    Date lastUpdated

    static mapping = {
    }
    static constraints = {
    }
    static TopicInitialApplyPlan DemoPlan = new TopicInitialApplyPlan([name: '2019年鄞州区教育科学规划课题申报计划'])
    static graphql = true
    static initList = []
}
