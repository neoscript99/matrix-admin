package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.ReviewRoundExpert
import com.feathermind.research.service.ReviewRoundExpertService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviewRoundExpert")
class ReviewRoundExportController extends DomainController<ReviewRoundExpert> {
    @Autowired
    ReviewRoundExpertService reviewRoundExpertService


    @Override
    ReviewRoundExpertService getDomainService() {
        return reviewRoundExpertService
    }
}
