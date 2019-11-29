package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 参数管理
 * @since 2018-10-16
 * @author wangchu
 */
@Service
class RoleMenuService extends AbstractService<RoleMenu> {
    @Autowired
    MenuService menuService
    @Autowired
    RoleService roleService

    RoleMenu saveEntity(RoleMenu roleMenu) {
        if (!count([eq: [['role', roleMenu.role], ['menu', roleMenu.menu]]]))
            Object.saveEntity(roleMenu)
    }

    List<RoleMenu> findByRoleAndMenu(Object roleId, Object menuId) {
        list([eq: [['role.id', roleId], ['menu.id', menuId]]])
    }

    void addRoleMenus(Object roleId, Object[] menuIds) {
        log.debug "addRoleMenu $roleId $menuIds"
        Role role = roleService.get(roleId)
        menuService.findByIds(menuIds).each { mm ->
            //app不为空的需要保存
            if (mm.app)
                saveEntity(new RoleMenu(role: role, menu: mm))
        }
    }

    void deleteRoleMenus(Object roleId, Object[] menuIds) {
        log.debug "deleteRoleMenu $roleId $menuIds"
        menuIds.each { menuId ->
            deleteByIds(findByRoleAndMenu(roleId, menuId)*.id)
        }
    }
}
