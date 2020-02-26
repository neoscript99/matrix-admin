package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.AchieveRoundResult
import com.feathermind.research.domain.res.ReviewRound
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AchieveRoundResultService extends AbstractService<AchieveRoundResult> {
    @Autowired
    ReviewRoundService reviewRoundService

    void calcRoundResult(String roundId) {
        calcRoundResult(reviewRoundService.get(roundId))
    }

    void calcRoundResult(ReviewRound round) {

    }
}
