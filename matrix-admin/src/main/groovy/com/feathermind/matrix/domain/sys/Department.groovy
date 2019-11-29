package com.feathermind.matrix.domain.sys

import com.feathermind.matrix.initializer.InitializeDomain
import grails.gorm.annotation.Entity
import groovy.transform.ToString

@Entity
@ToString(includePackage = false, includes = 'name')
@InitializeDomain
class Department {
    static final Department HEAD_OFFICE = new Department(name: '总部', seq: 1);
    String id
    String name;
    Integer seq;
    Boolean enabled = true

    static initList = [HEAD_OFFICE]
    static graphql = true
    static constraints = {
        name unique: true
    }
}
