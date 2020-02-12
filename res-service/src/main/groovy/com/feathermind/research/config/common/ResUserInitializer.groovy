package com.feathermind.research.config.common

import cn.hutool.core.util.StrUtil
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import com.feathermind.matrix.util.EncoderUtil
import com.feathermind.research.domain.res.*
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order

import static InitEntity.*
import static com.feathermind.research.domain.res.ResDept.*

@Order(200)
@CompileStatic(TypeCheckingMode.SKIP)
class ResUserInitializer extends AbstractDataInitializer implements DataInitializer {

    static final String DEFAULT_PASS = EncoderUtil.sha256('abc000')

    @Override
    boolean isInited() {
        return ResUser.findByAccount('sys-admin')
    }

    @Override
    void doInit() {
        initUser()
    }


    static ResUser resManagerUser = new ResUser(name: '科研系统管理员', account: 'sys-admin', idCard: '330201198810106666', cellPhone: '88121117', dept: headDept, password: DEFAULT_PASS)

    def initUser() {
        resManagerUser.save()
        new UserRole(resManagerUser, MAIN_MANAGER).save()
        new UserRole(resManagerUser, RES_USER).save()
        initDeptUser().each {
            new UserRole(it, InitEntity.DEPT_MANAGER).save()
        }
        initExportUser()
    }

    List<ResUser> initDeptUser() {
        List<ResUser> allUser = []
        def cateList = generalRepository.list(ResDeptType, [ne: [['code', 'external_agency']], order: ['seq']]).groupBy { it.cateCode }
        cateList.each { k, v ->
            def index = 0;
            generalRepository.list(ResDept, [inList: [['type', v]], order: ['seq']]).each {
                index++
                def account = k + StrUtil.padPre(index.toString(), 3, '0')
                def user = new ResUser(name: account, account: account, dept: it, password: DEFAULT_PASS).save()
                allUser.add(user)
            }
        }
        return allUser
    }

    def initExportUser() {
        (1..30).each {
            def index = StrUtil.padPre(it.toString(), 3, '0')
            def user = new ResUser(name: '评审专家' + index, account: 'EXP' + index, dept: expertDept, password: DEFAULT_PASS).save()
            new UserRole(user, InitEntity.EXPERT).save()
        }
    }
}
