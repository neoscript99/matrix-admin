package com.feathermind.research.config.common

import com.feathermind.matrix.domain.sys.Role

interface InitEntity {

    static MAIN_MANAGER = new Role(roleName: '科研系统管理员', roleCode: 'ResMainManager',
            description: '科研全业务管理功能', authorities: 'SysAdmin')

    static DEPT_MANAGER = new Role(roleName: '单位管理员', roleCode: 'ResDeptManager',
            description: '单位事务办理', authorities: 'resPackageAll')

    static RES_USER = new Role(roleName: '普通用户', roleCode: 'ResUser', enabled: false,
            description: '个人事务办理')

    static EXPERT = new Role(roleName: '评审专家', roleCode: 'ResExpert',
            description: '课题论文评审', authorities: 'AchieveExpertScoreAll,AchieveRead,ReviewRoundExpertRead')

}
