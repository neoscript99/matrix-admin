package org.yzedu.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yzedu.research.domain.res.YzUser
import org.yzedu.research.service.YzUserService

@RestController
@RequestMapping("/api/yzUser")
class YzUserController extends DomainController<YzUser> {
    @Autowired
    YzUserService yzUserService

    @PostMapping("/saveWithRoles")
    ResponseEntity<User> saveUserWithRoles(@RequestBody Map req) {
        def yzUser = yzUserService.save(req.user)
        return ResponseEntity.ok(userService.saveUserWithRoles(yzUser, req.roleIds))
    }

    @Override
    AbstractService<YzUser> getDomainService() {
        return yzUserService
    }
}
