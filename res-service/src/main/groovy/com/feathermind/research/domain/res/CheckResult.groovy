package com.feathermind.research.domain.res

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includePackage = false, includeNames = true, includes = 'id, success, desc')
@EqualsAndHashCode(includes = 'id')
class CheckResult {
    String id
    Boolean success
    String desc
    Date dateCreated
    Date lastUpdated

    static constraints = {
        desc nullable: true
    }
}
