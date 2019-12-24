package org.yzedu.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import org.yzedu.research.domain.res.Topic
import org.yzedu.research.domain.res.TopicWorkPlan

/**
 * 课题立项申请
 */
@Entity
class TopicInitialApply extends Apply{
    TopicWorkPlan plan
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
