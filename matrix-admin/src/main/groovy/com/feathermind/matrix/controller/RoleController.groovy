package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/role")
class RoleController extends DomainController<Role> {
    @Autowired
    RoleService roleService
    @Override
    AbstractService<Role> getDomainService() {
        return roleService
    }
}
