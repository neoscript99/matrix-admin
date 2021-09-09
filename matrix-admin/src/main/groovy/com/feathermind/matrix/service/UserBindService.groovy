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
    UserService userService
    @Autowired
    MatrixConfigProperties matrixConfigProperties

    UserBind getOrCreateUser(@NotNull UserBind newBind) {
        if (!newBind.openid && !newBind.nickname)
            throw new RuntimeException('openid和nickname不能为空')
        
        def oldBind = findBind(newBind.openid, newBind.unionid, null)

        if (oldBind) {
            newBind.user = oldBind.user
            BeanUtils.copyProperties(newBind, oldBind, GormRepository.domainUpdateIgnores)
            saveEntity(oldBind)
            return oldBind
        }

        //新用户
        def dept = Department.findBySeq(1) ?: Department.find {}
        newBind.user = new User(account: newBind.openid, name: newBind.nickname, dept: dept).save()
        saveEntity(newBind)
        roleService.findByCodes(matrixConfigProperties.defaultRoles.split(',')).each {
            new UserRole(newBind.user, it).save()
        }
        return newBind
    }

    UserBind findBind(String openId, String unionId, String phoneNumber) {
        //根据openid查找，如果已经绑定，更新信息
        //根据union查找
        def nope = '0x00000'
        return UserBind.findByOpenidOrUnionidOrPhoneNumber(openId ?: nope, unionId ?: nope, phoneNumber ?: nope)
    }
    //通过openId绑定手机号，修改user表account和cellPhone
    UserBind bindPhone(String openId, String unionId, String phoneNumber) {
        if (!openId && !phoneNumber)
            throw new RuntimeException('openId和phoneNumber不能为空')
        def bind = findBind(openId, unionId, null);
        if (bind) {
            bind.phoneNumber = phoneNumber;
            def existUser = userService.findByAccount(phoneNumber)
            if (existUser)
                bind.user = existUser;
            else {
                bind.user.account = phoneNumber;
                bind.user.cellPhone = phoneNumber;
            }
            saveEntity(bind)
        }
        return bind;
    }
}
