package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.*
import com.feathermind.matrix.service.bean.LoginResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
@Service
class UserService extends AbstractService<User> {
    @Autowired
    UserRoleService userRoleService

    @Transactional(readOnly = true)
    LoginResult login(String account, String password) {
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
            new LoginResult(success: false, error: msg)
        } else
            new LoginResult(success: true, user: user)
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
    /**
     * 删除角色
     */
    @Override
    Number deleteById(Serializable id) {
        userRoleService.deleteByUserId(id)
        super.deleteById(id)
    }

    /**
     * 删除角色
     */
    @Override
    Number deleteByIds(List idList) {
        userRoleService.deleteByUserIds(idList)
        super.deleteByIds(idList)
    }
    /**
     * 用户信息迁移
     * 由于User可能会被依赖系统继承，微信绑定的是基类，需要做迁移
     * 这里只处理了UserRole和UserBind，如果该用户产生了业务依赖不应该做删除处理
     * @param newId
     * @param oldId
     */
    void migrate(String oldId, User newUser) {
        getUserRoles(oldId).each {
            new UserRole(newUser, it).save()
        }
        generalRepository.list(UserBind, [eq: [['user.id', oldId]]]).each { ub ->
            ub.user = newUser;
            ub.save(flush: true);
        }
        deleteById(oldId)
    }

    /**
     * 检查手机号是否重复
     * @param userId
     * @param cellPhone
     * @return
     */
    boolean checkAccountUnique(String userId, String account) {
        def param = [eq: [['account', account]]]
        if (userId)
            param.ne = [['id', userId]]
        return count(param) == 0
    }
}
