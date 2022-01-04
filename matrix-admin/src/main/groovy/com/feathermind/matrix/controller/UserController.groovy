package com.feathermind.matrix.controller

import com.feathermind.matrix.config.MatrixConfigProperties
import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user")
class UserController extends DomainController<User> {
    @Autowired
    MatrixConfigProperties matrixConfigProperties

    @Override
    User save(@RequestBody Map map) {
        //新增用户时，使用初始化密码
        if (!map.id && !map.password)
            map.password = matrixConfigProperties.initPasswordHash
        return super.save(map)
    }

    @PostMapping("/saveWithRoles")
    User saveUserWithRoles(@RequestBody Map req) {
        writeAuthorize()
        return userService.saveUserWithRoles(req.user, req.roleIds)
    }

    @PostMapping("/resetPassword")
    ResBean resetPassword(@RequestBody Map req) {
        def password = matrixConfigProperties.initPasswordHash
        this.save([id      : req.userId,
                   password: password])
        return new ResBean()
    }

    @Override
    AbstractService<User> getDomainService() {
        return userService
    }
}
