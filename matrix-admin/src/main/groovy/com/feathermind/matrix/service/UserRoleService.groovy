package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.UserRole
import org.springframework.stereotype.Service

/**
 * @author wangchu
 */
@Service
class UserRoleService extends AbstractService<UserRole> {
    Number deleteByUserId(Serializable userId) {
        deleteMatch(eq: [['user.id', userId]])
    }

    Number deleteByUserIds(List userIdList) {
        deleteMatch(inList: [['user.id', userIdList]])
    }
}
