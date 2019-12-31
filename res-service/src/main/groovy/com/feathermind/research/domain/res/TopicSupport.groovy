package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果支撑材料
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'topic,support')
@EqualsAndHashCode(includes = 'id')
class TopicSupport {
    String id
    Topic topic
    AttachmentInfo support
    Integer seq

    Date dateCreated
    Date lastUpdated

    static mapping = {
        topic fetch: 'join', lazy: false
        support fetch: 'join', lazy: false
    }
}
