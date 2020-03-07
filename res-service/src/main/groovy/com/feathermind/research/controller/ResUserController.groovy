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

import static com.feathermind.research.config.common.InitEntity.*

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

    Map preList(Map criteria) {
        def user = this.getCurrentUser(true)
        def roles = this.tokenDetails.roles
        if (!roles.contains(MAIN_MANAGER.roleCode)) {
            if (!criteria.eq)
                criteria.eq = []
            criteria.eq << ['dept.id', user.dept.id]
        }
        return criteria
    }

    @PostMapping("/idCardCheck")
    ResponseEntity<Boolean> idCardCheck(@RequestBody Map req) {
        return ResponseEntity.ok(resUserService.checkIdCardUnique(req.id, req.dept?.id, req.idCard))
    }

    @PostMapping("/getAvailableExperts")
    ResponseEntity<List<ResUser>> getAvailableExperts() {
        return ResponseEntity.ok(resUserService.getAvailableExperts())
    }

    @Override
    AbstractService<ResUser> getDomainService() {
        return resUserService
    }
}
