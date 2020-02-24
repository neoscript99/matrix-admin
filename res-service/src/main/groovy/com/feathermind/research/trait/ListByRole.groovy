package com.feathermind.research.trait

import com.feathermind.matrix.controller.DomainController
import groovy.transform.SelfType

import static com.feathermind.research.config.common.InitEntity.DEPT_MANAGER
import static com.feathermind.research.config.common.InitEntity.EXPERT
import static com.feathermind.research.config.common.InitEntity.MAIN_MANAGER
import static com.feathermind.research.config.common.InitEntity.RES_USER

@SelfType(DomainController)
trait ListByRole {
    Map preList(Map criteria) {
        def user = this.getSessionUser(true)
        def roles = this.getToken().roles.split(',')
        if (roles.contains(MAIN_MANAGER.roleCode) || roles.contains(EXPERT.roleCode))
            return criteria

        if (!criteria.eq)
            criteria.eq = []
        if (roles.contains(DEPT_MANAGER.roleCode))
            criteria.eq << ['dept.id', user.dept.id]
        else if (roles.contains(RES_USER.roleCode))
            criteria.eq << ['personInCharge', user]
        else
            throw new RuntimeException('当前用户没有权限')
        return criteria
    }
}
