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

@Service
class UserBindService extends AbstractService<UserBind> {
    @Autowired
    RoleService roleService
    @Value('${matrix.defaultRoles}')
    String defaultRoles

    User getOrCreateUser(UserBind newBind) {
        def bind = UserBind.findByOpenid(newBind.openid);
        //如果已经绑定，更新信息
        if (bind) {
            newBind.user = bind.user
            BeanUtils.copyProperties(newBind, bind, GormRepository.domainUpdateIgnores)
            saveEntity(bind)
            return bind.user;
        }

        def uBind = UserBind.findByUnionid(newBind.unionid)
        if (uBind) {
            newBind.user = uBind.user
            saveEntity(newBind)
            return uBind.user
        }

        newBind.user = new User(account: newBind.openid, name: newBind.nickname, dept: Department.findBySeq(1)).save()
        saveEntity(newBind)
        roleService.findByCodes(defaultRoles.split(',')).each {
            new UserRole(newBind.user, it).save()
        }
        return newBind.user
    }
}
