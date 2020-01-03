package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.wf.Apply
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.ApplyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/apply")
class ApplyController extends DomainController<Apply> {
    @Autowired
    ApplyService applyService

    @Override
    AbstractService<Apply> getDomainService() {
        return applyService
    }
}
