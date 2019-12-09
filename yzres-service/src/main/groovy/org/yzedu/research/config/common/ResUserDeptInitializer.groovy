package org.yzedu.research.config.common

import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import groovy.transform.CompileStatic
import org.springframework.core.annotation.Order
import org.yzedu.research.domain.res.YzDept

@Order(200)
@CompileStatic
class ResUserDeptInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        return Role.findByRoleCode('YzResMainManager')
    }

    @Override
    void doInit() {
        initDept()
    }


    void initDept() {
        new YzDept(name: '鄞州区教研室', seq: 0, contact: '鲍老师', address: '鄞州区学府路5号鄞州教育综合服务楼306室',
                telephone: '88121117', shortDial: '666188')
    }
}
