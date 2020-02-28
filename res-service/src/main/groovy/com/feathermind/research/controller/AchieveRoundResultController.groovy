package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.AchieveRoundResult
import com.feathermind.research.service.AchieveRoundResultService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/achieveRoundResult")
class AchieveRoundResultController extends DomainController<AchieveRoundResult> {
    @Autowired
    AchieveRoundResultService achieveRoundResultService


    @Override
    AchieveRoundResultService getDomainService() {
        return achieveRoundResultService
    }
}
