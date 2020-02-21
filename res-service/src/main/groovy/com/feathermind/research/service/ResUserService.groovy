package com.feathermind.research.service

import com.feathermind.matrix.domain.sys.*
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.util.DateUtil
import com.feathermind.research.config.common.InitEntity
import com.feathermind.research.domain.res.ResUser
import com.feathermind.research.domain.res.ReviewRoundExpert
import org.springframework.stereotype.Service

@Service
class ResUserService extends AbstractService<ResUser> {
    /**
     * 同部门下身份证不能重复
     * @param userId
     * @param deptId
     * @param idCard
     * return 成功: true
     */
    boolean checkIdCardUnique(String userId, String deptId, String idCard) {
        def param = [eq: [['dept.id', deptId], ['idCard', idCard]]]
        if (userId)
            param.ne = [['id', userId]]
        return count(param) == 0
    }

    List getAvailableExperts() {
        ResUser.where {
            def ru = ResUser
            exists UserRole.where {
                def ur = UserRole
                role.roleCode == InitEntity.EXPERT.roleCode && user.id == ru.id
            }.id()
            notExists ReviewRoundExpert.where {
                def rre = ReviewRoundExpert
                expert.id == ru.id && round.endDay >= DateUtil.dayStr()
            }.id()
        }.list(sort: 'name')
    }
}
