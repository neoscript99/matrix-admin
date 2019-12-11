package org.yzedu.research.domain.res

import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 专家每轮评分结果
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'roundExpert,reviewApply')
@EqualsAndHashCode(includes = 'id')
class ReviewRoundScore {
    String id
    ReviewRoundExpert roundExpert
    Apply reviewApply
    Integer score
    Boolean passed

    Date dateCreated
    Date lastUpdated

    static mapping = {
        roundExpert fetch: 'join', lazy: false
        reviewApply fetch: 'join', lazy: false
    }
}
