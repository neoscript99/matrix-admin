package org.yzedu.research.config.common

import com.feathermind.matrix.domain.sys.Menu
import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.domain.sys.RoleMenu
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order

@Order(100)
@CompileStatic(TypeCheckingMode.SKIP)
class ResMenuRoleInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        return Role.findByRoleCode('YzResMainManager')
    }

    @Override
    void doInit() {
        initRole()
        initMenu()
    }

    static mainManager = new Role(roleName: '科研业务管理员', roleCode: 'YzResMainManager', description: '全部科研项目管理功能')
    static deptManager = new Role(roleName: '单位管理员', roleCode: 'YzResDeptManager', description: '单位事务办理')
    static resUser = new Role(roleName: '普通用户', roleCode: 'YzResUser', description: '个人事务办理')
    static expert = new Role(roleName: '评审专家', roleCode: 'YzResExpert', description: '课题论文评审')

    void initRole() {
        Role.list().each {
            it.enabled = false;
        }
        save(mainManager)
        save(deptManager)
        save(resUser)
        save(expert)
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
            new RoleMenu(role: mainManager, menu: it).save()
        }
    }

    List initBaseMenu() {
        Menu parentMenu = save(new Menu(label: '基础信息', seq: 20, parentId: rootMenu.id))
        def subList = [
                new Menu(label: '课题查询', app: 'Topic', seq: 30, parentId: parentMenu.id).save(),
                new Menu(label: '论文查询', app: 'Paper', seq: 40, parentId: parentMenu.id).save(),
        ]
        subList.each { new RoleMenu(resUser, it).save() }
        subList << new Menu(label: '用户管理', app: 'YzUser', seq: 10, parentId: parentMenu.id, icon: 'user').save()
        subList.each { new RoleMenu(deptManager, it).save() }
        subList << new Menu(label: '单位管理', app: 'YzDept', seq: 20, parentId: parentMenu.id, icon: 'apartment').save()
        return subList
    }

    List initTopicMenu() {
        Menu parentMenu = save(new Menu(label: '课题管理', seq: 30, parentId: rootMenu.id))
        def subList = [
                new Menu(label: '申报计划', app: 'WorkPlan', seq: 10, parentId: parentMenu.id).save(),
                new Menu(label: '立项申报', app: 'InitialApply', seq: 20, parentId: parentMenu.id).save(),
                new Menu(label: '结题申报', app: 'FinishApply', seq: 30, parentId: parentMenu.id).save(),
        ]

        subList.each {
            new RoleMenu(deptManager, it).save()
            new RoleMenu(resUser, it).save()
        }

        return subList
    }

    List initReviewMenu() {
        Menu parentMenu = save(new Menu(label: '评审流程', seq: 40, parentId: rootMenu.id))
        def subList = [
                new Menu(label: '课题评审', app: 'TopicReview', seq: 20, parentId: parentMenu.id).save(),
                new Menu(label: '论文评审', app: 'PaperReview', seq: 30, parentId: parentMenu.id).save(),
        ]

        subList.each {
            new RoleMenu(expert, it).save()
        }
        subList.addAll([
                new Menu(label: '评审计划', app: 'ReviewPlan', seq: 10, parentId: parentMenu.id).save(),
                new Menu(label: '结果查询', app: 'PaperReview', seq: 40, parentId: parentMenu.id).save(),
        ])

        subList.each {
            new RoleMenu(deptManager, it).save()
            new RoleMenu(resUser, it).save()
        }

        return subList
    }
}
