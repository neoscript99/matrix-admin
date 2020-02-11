package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.domain.wf.Apply
import com.feathermind.matrix.trait.AutoTime
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 论文基本信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'title')
@EqualsAndHashCode(includes = 'id')
class Paper implements AutoTime{
    String id
    String title
    ResUser author
    AttachmentInfo paperFile

    ReviewPlan reviewPlan
    CheckResult duplicateCheck

    static mapping = {
        author fetch: 'join', lazy: false
        paperFile fetch: 'join', lazy: false, cascade: 'delete'
        reviewPlan fetch: 'join', lazy: false
        duplicateCheck fetch: 'join', lazy: false
    }
    static constraints = {
        duplicateCheck nullable: true
    }
}
