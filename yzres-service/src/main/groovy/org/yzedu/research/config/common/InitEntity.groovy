package org.yzedu.research.config.common

import com.feathermind.matrix.domain.sys.Role

interface InitEntity {

    static MAIN_MANAGER = new Role(roleName: '科研业务管理员', roleCode: 'YzResMainManager', description: '全部科研项目管理功能')
    static DEPT_MANAGER = new Role(roleName: '单位管理员', roleCode: 'YzResDeptManager', description: '单位事务办理')
    static RES_USER = new Role(roleName: '普通用户', roleCode: 'YzResUser', description: '个人事务办理')
    static EXPERT = new Role(roleName: '评审专家', roleCode: 'YzResExpert', description: '课题论文评审')

}
