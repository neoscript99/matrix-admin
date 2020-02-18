package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.ReviewRound
import com.feathermind.research.service.ReviewRoundService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviewRound")
class ReviewRoundController extends DomainController<ReviewRound> {
    @Autowired
    ReviewRoundService reviewRoundService


    @Override
    ReviewRoundService getDomainService() {
        return reviewRoundService
    }
}
