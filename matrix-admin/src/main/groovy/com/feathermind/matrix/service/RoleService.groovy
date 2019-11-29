package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.Role
import org.springframework.stereotype.Service

/**
 * 参数管理
 * @since 2018-10-16
 * @author wangchu
 */
@Service
class RoleService extends AbstractService<Role> {
    List<Role> findByCodes(String[] codes) {
        if (codes && codes.length > 0)
            list(['in': [['roleCode', codes]]])
    }
}
