package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Menu
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.MenuNode
import com.feathermind.matrix.service.MenuService
import com.feathermind.matrix.service.RoleService
import com.feathermind.matrix.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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
    GormSessionBean gormSessionBean
    @Autowired
    UserService userService

    @PostMapping("/menuTree")
    ResponseEntity<MenuNode> menuTree() {
        def token = gormSessionBean.token
        def account = token.username;
        def user = userService.findByAccount(account);
        MenuNode menuNode
        if (user)
            menuNode = menuService.getUserTree(user)
        else {
            def roleList = roleService.findByCodes(token.roles.split(','))
            menuNode = menuService.getRolesTree(roleList)
        }
        ResponseEntity.ok(menuNode)
    }

    @Override
    AbstractService getDomainService() {
        return menuService
    }
}
