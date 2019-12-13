package org.yzedu.research.config.common

import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order
import org.yzedu.research.domain.res.YzDept
import org.yzedu.research.domain.res.YzUser

@Order(200)
@CompileStatic(TypeCheckingMode.SKIP)
class ResUserDeptInitializer extends AbstractDataInitializer implements DataInitializer {

    @Override
    boolean isInited() {
        return YzDept.findByName('鄞州区教研室')
    }

    @Override
    void doInit() {
        initDept()
        initUser()
    }


    static YzDept yzOfficeDept = new YzDept(name: '鄞州区教研室', seq: 0, contact: '鲍老师', address: '鄞州区学府路5号鄞州教育综合服务楼306室',
            telephone: '88121117', shortDial: '666188')

    void initDept() {
        yzOfficeDept.save()
    }

    static YzUser resManagerUser = new YzUser(name: '科研系统管理员', account: 'manager', phoneNumber: '88121117', dept: yzOfficeDept)

    def initUser() {
        resManagerUser.save()
        new UserRole(resManagerUser, ResMenuRoleInitializer.mainManager).save()
        new UserRole(resManagerUser, Role.NORMAL_USERS).save()
    }
}
