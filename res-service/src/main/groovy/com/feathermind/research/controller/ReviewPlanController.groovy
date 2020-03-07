package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.ReviewPlan
import com.feathermind.research.service.ReviewPlanService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviewPlan")
class ReviewPlanController extends DomainController<ReviewPlan> {
    @Autowired
    ReviewPlanService reviewPlanService

    @PostMapping("/listStarted")
    ResponseEntity<List<ReviewPlan>> listStarted() {
        readAuthorize()
        return ResponseEntity.ok(reviewPlanService.listStarted())
    }

    @Override
    ReviewPlanService getDomainService() {
        return reviewPlanService
    }
}
