package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.domain.sys.UserBind
import com.feathermind.matrix.repositories.GormRepository
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service

@Service
class UserBindService extends AbstractService<UserBind> {
    User getOrCreateUser(UserBind newBind) {
        def bind = UserBind.findByOpenid(newBind.openid);
        //如果已经绑定，更新信息
        if (bind) {
            newBind.user=bind.user
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
        return newBind.user
    }
}
