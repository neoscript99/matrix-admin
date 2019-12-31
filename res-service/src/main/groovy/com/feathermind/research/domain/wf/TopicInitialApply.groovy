package com.feathermind.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import com.feathermind.research.domain.res.Topic

/**
 * 课题立项申请
 */
@Entity
class TopicInitialApply extends Apply{
    com.feathermind.research.domain.res.TopicWorkPlan plan
    Topic topic
    //仅限申报重点课题填写
    String originTopicCode
    String originTopicName

    static mapping = {
        plan fetch: 'join', lazy: false
        topic fetch: 'join', lazy: false
    }
    static constraints = {
        originTopicCode nullable: true
        originTopicName nullable: true
    }
}
