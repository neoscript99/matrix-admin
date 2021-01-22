package com.feathermind.matrix.domain.sys

import com.feathermind.matrix.initializer.InitializeDomain
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includePackage = false, includes = 'name')
@InitializeDomain
@EqualsAndHashCode(includes = 'id')
class Department {
    String id
    String name;
    Integer seq;
    Boolean enabled = true
    static mapping = {
        tablePerHierarchy true
        sort 'seq'
    }
    static final Department HEAD_OFFICE = new Department(name: '总部', seq: 1);
    static initList = [HEAD_OFFICE]
    static constraints = {
        name unique: true
    }
}
