package org.yzedu.research.config.common

import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import com.feathermind.matrix.util.EncoderUtil
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order
import org.yzedu.research.domain.res.YzDept
import org.yzedu.research.domain.res.YzUser
import static org.yzedu.research.config.common.InitEntity.*

@Order(200)
@CompileStatic(TypeCheckingMode.SKIP)
class ResUserDeptInitializer extends AbstractDataInitializer implements DataInitializer {

    static final String DEFAULT_PASS = EncoderUtil.sha256('abc000')

    @Override
    boolean isInited() {
        return YzDept.findByName('鄞州区教研室')
    }

    @Override
    void doInit() {
        initDept()
        initUser()
    }


    static YzDept yzOfficeDept = new YzDept(name: '鄞州区教育科学研究室', seq: 0, contact: '鲍老师', address: '鄞州区学府路5号鄞州教育综合服务楼306室', telephone: '88121117', shortDial: '666188')
    static YzDept demoSchool = new YzDept(name: '鄞州中学', seq: 1, contact: '李老师', address: '鄞州区', telephone: '88888888', shortDial: '888888')
    static YzDept expertDept = new YzDept(name: '专家组', seq: 1, contact: '专家', address: '鄞州区', telephone: '88888888', shortDial: '888888')

    void initDept() {
        Department.list().each {
            it.enabled = false;
        }
        yzOfficeDept.save()
        demoSchool.save()
        expertDept.save()
    }

    static YzUser resManagerUser = new YzUser(name: '科研系统管理员', account: 'manager', phoneNumber: '88121117', dept: yzOfficeDept, password: DEFAULT_PASS)
    static YzUser demoSchoolUser = new YzUser(name: '高级中学管理员', account: 'yz_middle', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS)
    static YzUser expertUser = new YzUser(name: '评审专家', account: 'expert', phoneNumber: '88888888', dept: expertDept, password: DEFAULT_PASS)

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
        new YzUser(name: '成员1', account: '高级中学管理员01', idCard: '333000199012126661', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new YzUser(name: '成员2', account: '高级中学管理员02', idCard: '333000199012126662', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new YzUser(name: '成员3', account: '高级中学管理员03', idCard: '333000199012126663', phoneNumber: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
    }
}
