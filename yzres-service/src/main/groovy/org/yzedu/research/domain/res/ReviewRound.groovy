package org.yzedu.research.domain.res

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
    //对不通过本轮的成果按分数分段，得出更详细的等级结果
    Integer failClassifyNumber = 1
    String beginDay
    String endDay

    Date dateCreated
    Date lastUpdated

    static mapping = {
        plan fetch: 'join', lazy: false
    }
}
