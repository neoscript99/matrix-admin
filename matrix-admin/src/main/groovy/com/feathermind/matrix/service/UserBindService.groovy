package com.feathermind.matrix.service

import com.feathermind.matrix.config.MatrixConfigProperties
import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.domain.sys.UserBind
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.repositories.GormRepository
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

import javax.validation.constraints.NotNull

@Service
class UserBindService extends AbstractService<UserBind> {
    @Autowired
    RoleService roleService
    @Autowired
    MatrixConfigProperties matrixConfigProperties

    User getOrCreateUser(@NotNull UserBind newBind) {
        //根据openid查找，如果已经绑定，更新信息
        def oldBind = UserBind.findByOpenid(newBind.openid);
        //根据union查找
        if (!oldBind && StringUtils.hasText(newBind.unionid))
            oldBind = UserBind.findByUnionid(newBind.unionid)

        if (oldBind) {
            newBind.user = oldBind.user
            BeanUtils.copyProperties(newBind, oldBind, GormRepository.domainUpdateIgnores)
            saveEntity(oldBind)
            return oldBind.user
        }

        //新用户
        def dept = Department.findBySeq(1) ?: Department.find {}
        newBind.user = new User(account: newBind.openid, name: newBind.nickname, dept: dept).save()
        saveEntity(newBind)
        roleService.findByCodes(matrixConfigProperties.defaultRoles.split(',')).each {
            new UserRole(newBind.user, it).save()
        }
        return newBind.user
    }
}
