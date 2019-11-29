package com.feathermind.matrix.controller


import com.feathermind.matrix.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    UserService userService

    @PostMapping("/saveWithRoles")
    ResponseEntity<com.feathermind.matrix.domain.sys.User> saveUserWithRoles(@RequestBody Map param) {
        return ResponseEntity.ok(userService.saveUserWithRoles(param.user, param.roleIds))
    }
}
