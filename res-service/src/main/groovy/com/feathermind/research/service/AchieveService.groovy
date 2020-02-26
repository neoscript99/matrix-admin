package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired

class AchieveService<T> extends AbstractService<T> {
    @Autowired
    AchieveExpertScoreService achieveExpertScoreService
    @Autowired
    ReviewRoundService reviewRoundService

    List<T> listByRound(String roundId) {
        def round = reviewRoundService.get(roundId)
    }
}
