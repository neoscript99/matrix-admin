package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.service.ResDeptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.feathermind.research.domain.res.ResDept

@RestController
@RequestMapping("/api/resDept")
class ResDeptController extends DomainController<ResDept> {
    @Autowired
    ResDeptService resDeptService


    @Override
    AbstractService<ResDept> getDomainService() {
        return resDeptService
    }
}
