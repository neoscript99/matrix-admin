package com.feathermind.matrix.config.data

import com.feathermind.matrix.domain.sys.*
import com.feathermind.matrix.initializer.MatrixFlywayMigration
import org.springframework.stereotype.Component

/**
 * Created by Neo on 2017-08-22.
 */
@Component
class V0003__menu_init extends MatrixFlywayMigration{

    @Override
    void run() {

        Menu rootMenu = save(new Menu(label: 'Root', icon: 'folder'))

        initAdminMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: Role.ADMINISTRATORS, menu: it))
        }
        initNormalUsersMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: Role.NORMAL_USERS, menu: it))
            save(new RoleMenu(role: Role.ADMINISTRATORS, menu: it))
        }
        initPublicMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: Role.PUBLIC, menu: it))
            save(new RoleMenu(role: Role.NORMAL_USERS, menu: it))
            save(new RoleMenu(role: Role.ADMINISTRATORS, menu: it))
        }
    }

    private List initAdminMenu(def rootId) {
        def sys = save(new Menu(label: '系统设置', seq: 90, parentId: rootId, icon: 'control'))

        [
                new Menu(label: '用户管理', app: 'User', seq: 33, parentId: sys.id, icon: 'usergroup-delete'),
                new Menu(label: '用户角色', app: 'UserRole', seq: 44, parentId: sys.id, icon: 'deployment-unit'),
                new Menu(label: '角色管理', app: 'Role', seq: 11, parentId: sys.id, icon: 'deployment-unit'),
                new Menu(label: '机构管理', app: 'Dept', seq: 22, parentId: sys.id, icon: 'apartment'),
                //new Menu(label: '发布通知', app: 'Note', seq: 55, parentId: sys.id, icon: 'notification'),
                new Menu(label: '参数维护', app: 'Param', seq: 66, parentId: sys.id, icon: 'setting')
        ]
    }

    private List initNormalUsersMenu(def rootId) {

        [
                new Menu(label: '个人设置', app: 'Profile', seq: 99, parentId: rootId, icon: 'user')
        ]
    }

    private List initPublicMenu(def rootId) {
        //这个不带通知，默认不显示
        save(new Menu(label: '欢迎页面', app: 'About', seq: 11, parentId: rootId, icon: 'home'))
        [
                new Menu(label: '首页', app: 'Welcome', seq: 10, parentId: rootId, icon: 'home')
        ]
    }
}
