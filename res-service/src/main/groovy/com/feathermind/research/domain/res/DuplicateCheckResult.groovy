package com.feathermind.research.domain.res

import com.feathermind.matrix.trait.AutoTime
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includePackage = false, includeNames = true, includes = 'id, success, desc')
@EqualsAndHashCode(includes = 'id')
class DuplicateCheckResult implements AutoTime {
    String id
    Boolean success
    String desc

    static constraints = {
        desc nullable: true, maxSize: 256
    }
}
