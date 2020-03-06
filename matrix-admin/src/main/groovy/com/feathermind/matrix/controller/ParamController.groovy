package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Param
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.ParamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/param")
class ParamController extends DomainController<Param> {
    @Autowired
    ParamService paramService

    @PostMapping("/listExtra")
    ResponseEntity<List<Map>> listExtra() {
        def list = [[code: 'EnvironmentProfiles', name: '环境配置方案', value: paramService.profiles]]
        return ResponseEntity.ok(list)
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
