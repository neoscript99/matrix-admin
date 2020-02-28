package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.AchieveExpertScore
import com.feathermind.research.domain.res.ReviewRound
import org.springframework.stereotype.Service

@Service
class AchieveExpertScoreService extends AbstractService<AchieveExpertScore> {
    List<AchieveExpertScore> findByRound(ReviewRound round) {
        AchieveExpertScore.findAll(fetch: [achieve: 'join']) {
            roundExpert.round == round
        }
    }
}
