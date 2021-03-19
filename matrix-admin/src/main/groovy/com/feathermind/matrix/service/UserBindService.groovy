package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.domain.sys.UserBind
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.repositories.GormRepository
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

import javax.validation.constraints.NotNull

@Service
class UserBindService extends AbstractService<UserBind> {
    @Autowired
    RoleService roleService
    @Value('${matrix.defaultRoles}')
    String defaultRoles

    User getOrCreateUser(@NotNull UserBind newBind) {
        //根据openid查找，如果已经绑定，更新信息
        def bind = UserBind.findByOpenid(newBind.openid);
        if (bind) {
            newBind.user = bind.user
            BeanUtils.copyProperties(newBind, bind, GormRepository.domainUpdateIgnores)
            saveEntity(bind)
            return bind.user;
        }
        //根据union查找
        if (StringUtils.hasText(newBind.unionid)) {
            def uBind = UserBind.findByUnionid(newBind.unionid)
            if (uBind) {
                newBind.user = uBind.user
                saveEntity(newBind)
                return uBind.user
            }
        }
        //新用户
        newBind.user = new User(account: newBind.openid, name: newBind.nickname, dept: Department.findBySeq(1)).save()
        saveEntity(newBind)
        roleService.findByCodes(defaultRoles.split(',')).each {
            new UserRole(newBind.user, it).save()
        }
        return newBind.user
    }
}
