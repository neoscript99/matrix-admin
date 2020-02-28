package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.ReviewRound
import com.feathermind.research.service.AchieveRoundResultService
import com.feathermind.research.service.ReviewRoundService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviewRound")
class ReviewRoundController extends DomainController<ReviewRound> {
    @Autowired
    ReviewRoundService reviewRoundService
    @Autowired
    AchieveRoundResultService achieveRoundResultService

    @PostMapping("/runResult")
    ResponseEntity<ReviewRound> runResult(@RequestBody Map entityMap) {
        String id = entityMap.id;
        Thread.start {
            achieveRoundResultService.calcRoundResult(id)
        }
        return ResponseEntity.ok(domainService.save([id: id, runStatusCode: 'running']))
    }

    @Override
    ReviewRoundService getDomainService() {
        return reviewRoundService
    }
}
