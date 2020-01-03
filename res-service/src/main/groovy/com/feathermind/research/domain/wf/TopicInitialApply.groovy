package com.feathermind.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import com.feathermind.research.domain.res.InitialPlan
import grails.gorm.annotation.Entity

/**
 * 课题立项申请
 */
@Entity
class TopicInitialApply extends Apply{
    InitialPlan plan
    //仅限申报重点课题填写
    String originTopicCode
    String originTopicName

    static mapping = {
        plan fetch: 'join', lazy: false
    }
    static constraints = {
        originTopicCode nullable: true
        originTopicName nullable: true
    }
}
