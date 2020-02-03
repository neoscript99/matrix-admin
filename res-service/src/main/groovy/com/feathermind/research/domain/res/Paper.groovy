package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 论文基本信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'title')
@EqualsAndHashCode(includes = 'id')
class Paper {
    String id
    String title
    ResUser author
    AttachmentInfo paperFile
    String paperStatusCode

    ReviewPlan reviewPlan
    Apply reviewApply
    CheckResult duplicateCheck
    Date dateCreated
    Date lastUpdated

    static mapping = {
        author fetch: 'join', lazy: false
        paperFile fetch: 'join', lazy: false, cascade: 'delete'
        reviewApply fetch: 'join', lazy: false
        reviewPlan fetch: 'join', lazy: false
        duplicateCheck fetch: 'join', lazy: false
    }
    static constraints = {
        duplicateCheck nullable: true
    }
}
