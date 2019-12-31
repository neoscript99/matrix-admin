package com.feathermind.matrix.controller

import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user")
class UserController extends DomainController<User> {

    @PostMapping("/saveWithRoles")
    ResponseEntity<User> saveUserWithRoles(@RequestBody Map req) {
        return ResponseEntity.ok(userService.saveUserWithRoles(req.user, req.roleIds))
    }

    @PostMapping("/resetPassword")
    ResponseEntity<ResBean> resetPassword(@RequestBody Map req) {
        def user = userService.get(req.userId)
        user.password=req.passwordHash
        userService.saveEntity(user)
        return ResponseEntity.ok(new ResBean())
    }

    @Override
    AbstractService<User> getDomainService() {
        return userService
    }
}
