package com.feathermind.matrix.service

import com.feathermind.matrix.config.MatrixConfigProperties
import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.domain.sys.UserBind
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.repositories.GormRepository
import org.hibernate.SessionFactory
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.validation.constraints.NotNull

@Service
class UserBindService extends AbstractService<UserBind> {
    @Autowired
    RoleService roleService
    @Autowired
    UserService userService
    @Autowired
    MatrixConfigProperties matrixConfigProperties
    @Autowired
    SessionFactory sessionFactory

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

        def existUser = userService.findByAccount(newBind.openid)
        if (existUser) {
            newBind.user = existUser
        } else {
            //新用户
            def dept = Department.findBySeq(1) ?: Department.find {}
            newBind.user = new User(account: newBind.openid, name: newBind.nickname, dept: dept).save()
            roleService.findByCodes(matrixConfigProperties.defaultRoles.split(',')).each {
                new UserRole(newBind.user, it).save()
            }
        }
        return saveEntity(newBind)
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
            //绑定account为当前手机号的user，可以支持先添加user设权限，再做绑定
            def existUser = userService.findByAccount(phoneNumber)
            if (existUser) {
                def oldId = bind.user.id;
                bind.user = existUser;
                //通过新线程启动新事务，内部回滚不影响外部事务
                Thread.start {
                    try {
                        sleep(200);
                        userService.deleteById(oldId)
                    } catch (Throwable t) {
                        log.error('绑定手机帐号后，无法删除原帐号，不影响业务流程', t)
                    }
                }
            } else {
                bind.user.account = phoneNumber;
                bind.user.cellPhone = phoneNumber;

                userService.saveEntity(bind.user)
            }
        }
        return bind;
    }
}
