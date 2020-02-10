package com.feathermind.research.config.dev

import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import com.feathermind.research.config.common.InitEntity
import com.feathermind.research.config.common.ResUserInitializer
import com.feathermind.research.domain.res.ResUser
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order

import static com.feathermind.research.domain.res.ResDept.*

@Order(200)
@CompileStatic(TypeCheckingMode.SKIP)
class ResDevUserInitializer extends AbstractDataInitializer implements DataInitializer {

    static final String DEFAULT_PASS = ResUserInitializer.DEFAULT_PASS

    @Override
    boolean isInited() {
        return ResUser.findByAccount('dept-admin')
    }

    @Override
    void doInit() {
        initUser()
    }


    static ResUser demoSchoolUser = new ResUser(name: '单位管理员', account: 'dept-admin', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS)
    static ResUser expertUser = new ResUser(name: '评审专家', account: 'expert', cellPhone: '88888888', dept: expertDept, password: DEFAULT_PASS)

    def initUser() {
        demoSchoolUser.save()
        expertUser.save()
        new UserRole(demoSchoolUser, InitEntity.DEPT_MANAGER).save()
        new UserRole(demoSchoolUser, InitEntity.RES_USER).save()
        new UserRole(expertUser, InitEntity.EXPERT).save()
        initDemoSchoolMember()
    }

    def initDemoSchoolMember() {
        new ResUser(name: '成员1', account: '高级中学管理员01', idCard: '333000199012126661', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new ResUser(name: '成员2', account: '高级中学管理员02', idCard: '333000199012126662', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new ResUser(name: '成员3', account: '高级中学管理员03', idCard: '333000199012126663', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
    }
}