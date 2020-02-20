package com.feathermind.research.service

import com.feathermind.research.domain.res.ReviewRound
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewRoundService extends PlanService<ReviewRound> {
    @Autowired
    ReviewRoundExpertService reviewRoundExpertService

    @Override
    ReviewRound save(Map map) {
        def round = super.save(map)
        reviewRoundExpertService.saveRoundExpert(round, map.experts)
        return round
    }
}
