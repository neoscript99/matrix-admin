package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
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
    ResUser personInCharge
    AttachmentInfo paperFile

    ReviewPlan reviewPlan
    CheckResult duplicateCheck
    ResDept dept

    static mapping = {
        personInCharge fetch: 'join', lazy: false
        paperFile fetch: 'join', lazy: false, cascade: 'delete'
        reviewPlan fetch: 'join', lazy: false
        duplicateCheck fetch: 'join', lazy: false
        dept fetch: 'join', lazy: false
    }
    static constraints = {
        duplicateCheck nullable: true
    }
}
