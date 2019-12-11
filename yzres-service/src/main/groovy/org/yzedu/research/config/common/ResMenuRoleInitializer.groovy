package org.yzedu.research.config.common

import com.feathermind.matrix.domain.sys.Role
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

    void initRole() {
        Role.list().each {
            it.enabled = false;
        }
        new Role(roleName: '鄞州区管理员', roleCode: 'YzResMainManager', description: '项目管理系统全部功能').save()
        new Role(roleName: '单位管理员', roleCode: 'YzResDeptManager', description: '提交本单位相关申请，查询评比结果').save()
        new Role(roleName: '评审专家', roleCode: 'YzResExpert', description: '课题论文评比').save()
    }

    def initMenu() {}
}
