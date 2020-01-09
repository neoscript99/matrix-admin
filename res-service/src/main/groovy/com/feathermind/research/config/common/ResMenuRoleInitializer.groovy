package com.feathermind.research.config.common

import com.feathermind.matrix.domain.sys.Menu
import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.domain.sys.RoleMenu
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order
import static InitEntity.*

@Order(100)
@CompileStatic(TypeCheckingMode.SKIP)
class ResMenuRoleInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        return Role.findByRoleCode('ResMainManager')
    }

    @Override
    void doInit() {
        initRole()
        initMenu()
    }

    void initRole() {
        Role.list().each {
            it.enabled = false;
        }
        save(MAIN_MANAGER)
        save(DEPT_MANAGER)
        save(RES_USER)
        save(EXPERT)
    }

    static Menu rootMenu

    def initMenu() {
        rootMenu = Menu.findByParentIdIsNull()
        def allList = [];
        allList.addAll(initBaseMenu())
        allList.addAll(initTopicMenu())
        allList.addAll(initReviewMenu())
        allList.each {
            new RoleMenu(role: Role.ADMINISTRATORS, menu: it).save()
            new RoleMenu(role: MAIN_MANAGER, menu: it).save()
        }
        RoleMenu.findAllByRole(Role.NORMAL_USERS).each {
            new RoleMenu(RES_USER, it.menu).save()
            new RoleMenu(DEPT_MANAGER, it.menu).save()
            new RoleMenu(EXPERT, it.menu).save()
            new RoleMenu(MAIN_MANAGER, it.menu).save()
        }
    }

    List initBaseMenu() {
        Menu parentMenu = save(new Menu(label: '基础信息', seq: 20, parentId: rootMenu.id))
        def subList = [
                new Menu(label: '课题查询', app: 'Topic', seq: 30, parentId: parentMenu.id, icon: 'book').save(),
                new Menu(label: '论文查询', app: 'Paper', seq: 40, parentId: parentMenu.id, icon: 'solution').save(),
        ]
        subList.each { new RoleMenu(RES_USER, it).save() }
        subList << new Menu(label: '用户管理', app: 'ResUser', seq: 10, parentId: parentMenu.id, icon: 'user').save()
        subList.each { new RoleMenu(DEPT_MANAGER, it).save() }
        subList << new Menu(label: '单位管理', app: 'ResDept', seq: 20, parentId: parentMenu.id, icon: 'apartment').save()
        return subList
    }

    List initTopicMenu() {
        Menu parentMenu = save(new Menu(label: '立项管理', seq: 30, parentId: rootMenu.id))
        def subList = [
                new Menu(label: '申报计划', app: 'InitialPlan', seq: 10, parentId: parentMenu.id, icon: 'schedule').save(),
                new Menu(label: '立项流程', app: 'InitialApply', seq: 20, parentId: parentMenu.id, icon: 'branches').save(),
                new Menu(label: '结题流程', app: 'FinishApply', seq: 30, parentId: parentMenu.id, icon: 'branches').save(),
        ]

        subList.each {
            new RoleMenu(DEPT_MANAGER, it).save()
            new RoleMenu(RES_USER, it).save()
        }

        return subList
    }

    List initReviewMenu() {
        Menu parentMenu = save(new Menu(label: '评比管理', seq: 40, parentId: rootMenu.id))
        def subList = [
                new Menu(label: '课题评比', app: 'TopicReview', seq: 20, parentId: parentMenu.id, icon: 'area-chart').save(),
                new Menu(label: '论文评比', app: 'PaperReview', seq: 30, parentId: parentMenu.id, icon: 'line-chart').save(),
        ]

        subList.each {
            new RoleMenu(EXPERT, it).save()
        }
        subList.addAll([
                new Menu(label: '评比计划', app: 'ReviewPlan', seq: 10, parentId: parentMenu.id, icon: 'schedule').save(),
                new Menu(label: '结果查询', app: 'PaperReview', seq: 40, parentId: parentMenu.id, icon: 'table').save(),
        ])

        subList.each {
            new RoleMenu(DEPT_MANAGER, it).save()
            new RoleMenu(RES_USER, it).save()
        }

        return subList
    }
}
