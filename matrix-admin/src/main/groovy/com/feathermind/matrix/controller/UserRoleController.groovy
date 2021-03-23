package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.UserRoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/userRole")
class UserRoleController extends DomainController<UserRole> {
    @Autowired
    UserRoleService userRoleService
    @Override
    AbstractService<UserRole> getDomainService() {
        return userRoleService
    }
}
