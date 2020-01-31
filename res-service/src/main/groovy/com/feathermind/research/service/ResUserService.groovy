package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.ResUser
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
}
