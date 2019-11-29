package com.feathermind.matrix.domain.sys

import com.feathermind.matrix.initializer.InitializeDomain
import grails.gorm.annotation.Entity

@Entity
@InitializeDomain
/**
 * 权限控制表
 */
class Authorization {
    String id
    //include exclude
    String authType
    /**
     * @see Token#userType
     */
    String userType
    String roleName
    //domain name | other
    String resourceName
    //self | org | all
    String regionName
    Boolean queryAllow
    Boolean mutationAllow
    Date lastUpdated

    static initList = []
}
