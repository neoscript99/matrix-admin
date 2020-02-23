package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.AchieveReviewScore
import com.feathermind.research.service.AchieveReviewScoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/achieveReviewScore")
class AchieveReviewScoreController extends DomainController<AchieveReviewScore> {
    @Autowired
    AchieveReviewScoreService achieveReviewScoreService


    @Override
    AchieveReviewScoreService getDomainService() {
        return achieveReviewScoreService
    }
}
