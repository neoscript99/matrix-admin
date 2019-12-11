package org.yzedu.research.domain.res

import grails.gorm.annotation.Entity

import com.feathermind.matrix.initializer.InitializeDomain
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题立项申报计划
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'planName')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = 'dev')
class TopicWorkPlan {
    String id
    String planName
    //立项年度
    Integer planYear
    //课题类别 字典id
    String topicCateId
    //申报开始日期
    String planBeginDay
    //申报截止日期
    String planEndDay
    //结题截止日期
    String finishDeadline

    /**
     * 单位申报数比例设置
     * 单位申报数*比例 计算取整方式：1、上浮 ceil 2、四舍五入 round 3、截断 floor
     * 可增加字典项，暂时不做处理
     */
    //String deptNumberRate
    //String roundMethod

    Date dateCreated
    Date lastUpdated

    static mapping = {
    }
    static constraints = {
    }
    static DemoPlan = new TopicWorkPlan([planName      : '2020年鄞州区教育科学规划课题申报计划',
                                         planYear      : 2020, topicCateId: 'YZGH',
                                         planBeginDay  : '2019-12-01', planEndDay: '2020-03-10',
                                         finishDeadline: '2021-12-31'])
    static initList = [DemoPlan]
}
