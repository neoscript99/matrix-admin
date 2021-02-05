package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.wf.ApplyLog
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.ApplyLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/applyLog")
class ApplyLogController extends DomainController<ApplyLog> {
    @Autowired
    ApplyLogService applyLogService

    @Override
    AbstractService<ApplyLog> getDomainService() {
        return applyLogService
    }
}
