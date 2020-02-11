package com.feathermind.research.domain.res

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 课题成果团队成员信息
 * 直接放到主表中，逗号分隔id
 */
@ToString(includePackage = false, includeNames = true, includes = 'topicAchieve,member')
@EqualsAndHashCode(includes = 'id')
class TopicAchieveMember {
    String id

    TopicAchieve topicAchieve
    ResUser member

    Date dateCreated
    Date lastUpdated

    static mapping = {
        topicAchieve fetch: 'join', lazy: false
        member fetch: 'join', lazy: false
    }
}
