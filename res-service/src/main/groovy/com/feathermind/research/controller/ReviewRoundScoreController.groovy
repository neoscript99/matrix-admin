package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.ReviewRoundScore
import com.feathermind.research.service.ReviewRoundScoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviewRoundScore")
class ReviewRoundScoreController extends DomainController<ReviewRoundScore> {
    @Autowired
    ReviewRoundScoreService reviewRoundScoreService


    @Override
    ReviewRoundScoreService getDomainService() {
        return reviewRoundScoreService
    }
}
