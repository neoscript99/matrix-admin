package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.ResDeptType
import com.feathermind.research.service.ResDeptTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/resDeptType")
class ResDeptTypeController extends DomainController<ResDeptType> {
    @Autowired
    ResDeptTypeService resDeptTypeService


    @Override
    AbstractService<ResDeptType> getDomainService() {
        return resDeptTypeService
    }
}
