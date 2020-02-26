package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.AchieveExpertScore
import com.feathermind.research.service.AchieveExpertScoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/achieveExpertScore")
class AchieveExpertScoreController extends DomainController<AchieveExpertScore> {
    @Autowired
    AchieveExpertScoreService achieveExpertScoreService


    @Override
    AchieveExpertScoreService getDomainService() {
        return achieveExpertScoreService
    }
}
