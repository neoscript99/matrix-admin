package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
@Service
class UserService extends AbstractService<User> {

    @Transactional(readOnly = true)
    Map login(String account, String password) {
        def user = findByAccount(account)
        String msg;
        if (user) {
            if (!user.enabled)
                msg = '用户帐号失效';
            if (user.password != password)
                msg = '密码错误'
        } else
            msg = '用户名不存在'

        if (msg) {
            log.warn("用户登录失败：$msg")
            [success: false, error: msg]
        } else
            [success: true, user: user]
    }

    @Transactional(readOnly = true)
    User findByAccount(String account) {
        findFirst([eq: [['account', account]]])
    }

    @Transactional(readOnly = true)
    List<Role> getUserRoles(String userId) {
        return getUserRoles(get(userId))
    }

    @Transactional(readOnly = true)
    List<Role> getUserRoles(User user) {
        UserRole.findAllByUser(user)*.role
    }

    @Transactional(readOnly = true)
    String getUserRoleCodes(User user) {
        //如果当前用户未配置角色，视同CAS登录无帐号的情况
        def roles = getUserRoles(user)
        (roles*.roleCode).join(',')
    }

    User saveUserWithRoles(Map userMap, List<String> roleIds) {
        def user = save(userMap)
        return saveUserWithRoles(user, roleIds);
    }

    User saveUserWithRoles(User user, List<String> roleIds) {
        generalRepository.deleteMatch(UserRole, [eq: [['user.id', user.id]]])
        roleIds.each {
            new UserRole(user: user, role: Role.get(it)).save()
        }
        // 清楚该用户缓存
        return user;
    }
}
