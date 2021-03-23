package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Menu
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.MenuNode
import com.feathermind.matrix.service.MenuService
import com.feathermind.matrix.service.RoleService
import com.feathermind.matrix.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/menu")
class MenuController extends DomainController<Menu> {
    @Autowired
    MenuService menuService
    @Autowired
    RoleService roleService
    @Autowired
    UserService userService

    @PostMapping("/menuTree")
    MenuNode menuTree() {
        MenuNode menuNode
        if (tokenDetails.user)
            menuNode = menuService.getUserTree(tokenDetails.user)
        else {
            def roleList = roleService.findByCodes(tokenDetails.roles)
            menuNode = menuService.getRolesTree(roleList)
        }
        menuNode
    }

    @Override
    AbstractService getDomainService() {
        return menuService
    }
}
