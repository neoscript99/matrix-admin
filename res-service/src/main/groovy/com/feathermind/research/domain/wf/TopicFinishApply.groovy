package com.feathermind.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity

/**
 * 课题结题申请
 */
@Entity
class TopicFinishApply extends Apply {
    TopicInitialApply initialApply

    static mapping = {
        initialApply fetch: 'join', lazy: false
    }
}
