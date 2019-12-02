package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Param
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.ParamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/param")
class ParamController extends DomainController<Param> {
    @Autowired
    ParamService paramService
    @Override
    AbstractService<Param> getDomainService() {
        return paramService
    }
}
