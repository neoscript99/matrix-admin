package com.feathermind.research.trait

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.util.MatrixException
import groovy.transform.SelfType

import static com.feathermind.research.config.common.InitEntity.DEPT_MANAGER
import static com.feathermind.research.config.common.InitEntity.EXPERT
import static com.feathermind.research.config.common.InitEntity.MAIN_MANAGER
import static com.feathermind.research.config.common.InitEntity.RES_USER

@SelfType(DomainController)
trait ListByRole {
    Map preList(Map criteria) {
        def user = this.getCurrentUser(true)
        def roles = this.tokenDetails.roles
        if (roles.contains(MAIN_MANAGER.roleCode) || roles.contains(EXPERT.roleCode))
            return criteria

        if (!criteria.eq)
            criteria.eq = []
        if (roles.contains(DEPT_MANAGER.roleCode))
            criteria.eq << ['dept.id', user.dept.id]
        else if (roles.contains(RES_USER.roleCode))
            criteria.eq << ['personInCharge', user]
        else
            throw new MatrixException('AuthorizeFail', '当前用户没有权限')
        return criteria
    }
}
