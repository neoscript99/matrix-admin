package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.ResUser
import com.feathermind.research.service.ResUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/resUser")
class ResUserController extends DomainController<ResUser> {
    @Autowired
    ResUserService resUserService

    @PostMapping("/saveWithRoles")
    ResponseEntity<ResUser> saveUserWithRoles(@RequestBody Map req) {
        def resUser = resUserService.save(req.user)
        return ResponseEntity.ok(userService.saveUserWithRoles(resUser, req.roleIds))
    }

    @Override
    AbstractService<ResUser> getDomainService() {
        return resUserService
    }
}
