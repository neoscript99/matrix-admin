package com.feathermind.research.domain.res


import com.feathermind.matrix.trait.AutoTime
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 成果基本信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'name')
@EqualsAndHashCode(includes = 'id')
class Achieve implements AutoTime{
    String id
    String name
    ResUser personInCharge

    ReviewPlan reviewPlan
    DuplicateCheckResult duplicateCheck
    ResDept dept

    static mapping = {
        personInCharge fetch: 'join', lazy: false
        reviewPlan fetch: 'join', lazy: false
        duplicateCheck fetch: 'join', lazy: false
        dept fetch: 'join', lazy: false
    }
    static constraints = {
        duplicateCheck nullable: true
    }
}
