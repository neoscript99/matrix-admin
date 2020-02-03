package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题组成员信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'topic,member')
@EqualsAndHashCode(includes = 'id')
class TopicMember {
    String id

    Topic topic
    ResUser member

    Date dateCreated
    Date lastUpdated

    static mapping = {
        topic fetch: 'join', lazy: false
        member fetch: 'join', lazy: false
    }
}
