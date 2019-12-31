package com.feathermind.research.config.common

import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import com.feathermind.matrix.util.EncoderUtil
import com.feathermind.research.domain.res.ResUser
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order
import com.feathermind.research.domain.res.ResDept

import static InitEntity.*

@Order(200)
@CompileStatic(TypeCheckingMode.SKIP)
class ResUserDeptInitializer extends AbstractDataInitializer implements DataInitializer {

    static final String DEFAULT_PASS = EncoderUtil.sha256('abc000')

    @Override
    boolean isInited() {
        return ResDept.findByName('教育科学研究室')
    }

    @Override
    void doInit() {
        initDept()
        initUser()
    }


    static ResDept headDept = new ResDept(name: '教育科学研究室', seq: 0, contact: '鲍老师', address: '鄞州区学府路5号鄞州教育综合服务楼306室', telephone: '88121117', shortDial: '666188')
    static ResDept demoSchool = new ResDept(name: '高级中学', seq: 1, contact: '李老师', address: '鄞州区', telephone: '88888888', shortDial: '888888')
    static ResDept expertDept = new ResDept(name: '专家组', seq: 1, contact: '专家', address: '鄞州区', telephone: '88888888', shortDial: '888888')

    void initDept() {
        Department.list().each {
            it.enabled = false;
        }
        headDept.save()
        demoSchool.save()
        expertDept.save()
    }

    static ResUser resManagerUser = new ResUser(name: '科研系统管理员', account: 'manager', phoneNumber: '88121117', dept: headDept, password: DEFAULT_PASS)
    static ResUser demoSchoolUser = new ResUser(name: '高级中学管理员', account: 'adv_middle', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS)
    static ResUser expertUser = new ResUser(name: '评审专家', account: 'expert', phoneNumber: '88888888', dept: expertDept, password: DEFAULT_PASS)

    def initUser() {
        resManagerUser.save()
        demoSchoolUser.save()
        expertUser.save()
        new UserRole(resManagerUser, MAIN_MANAGER).save()
        new UserRole(resManagerUser, RES_USER).save()
        new UserRole(demoSchoolUser, DEPT_MANAGER).save()
        new UserRole(demoSchoolUser, RES_USER).save()
        new UserRole(expertUser, EXPERT).save()
        initDemoSchoolMember()
    }

    def initDemoSchoolMember() {
        new ResUser(name: '成员1', account: '高级中学管理员01', idCard: '333000199012126661', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new ResUser(name: '成员2', account: '高级中学管理员02', idCard: '333000199012126662', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new ResUser(name: '成员3', account: '高级中学管理员03', idCard: '333000199012126663', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
    }
}
