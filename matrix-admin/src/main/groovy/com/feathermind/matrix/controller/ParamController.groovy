package com.feathermind.matrix.controller

import com.feathermind.matrix.config.MatrixConfigProperties
import com.feathermind.matrix.domain.sys.Param
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.ParamService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/param")
class ParamController extends DomainController<Param> {
    @Autowired
    ParamService paramService
    @Autowired
    private Environment env;
    @Autowired
    MatrixConfigProperties matrixConfigProperties

    @PostMapping("/listExtra")
    List<Map> listExtra() {
        //autowire的proxy包含$$beanFactory，先删除
        MatrixConfigProperties copyConfig = new MatrixConfigProperties()
        BeanUtils.copyProperties(matrixConfigProperties, copyConfig)
        def list = [
                [code: 'EnvironmentProfiles', name: '环境配置方案', value: env.activeProfiles],
                [code: 'MatrixConfig', name: 'MatrixConfig', value: copyConfig]
        ]
        return list
    }

    /**
     * 开放读权限
     */
    @Override
    void readAuthorize() {
    }

    @Override
    void readOneAuthorize() {
    }

    @Override
    AbstractService<Param> getDomainService() {
        return paramService
    }
}
