package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.Paper
import com.feathermind.research.service.PaperService
import com.feathermind.research.trait.ListByRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/paper")
class PaperController extends DomainController<Paper> implements ListByRole {
    @Autowired
    PaperService paperService

    @Override
    AbstractService<Paper> getDomainService() {
        return paperService
    }
}
