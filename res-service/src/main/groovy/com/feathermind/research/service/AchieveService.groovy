package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.Achieve
import com.feathermind.research.domain.res.AchieveRoundResult
import com.feathermind.research.domain.res.ReviewRound

class AchieveService<T extends Achieve> extends AbstractService<T> {

    List<T> listByRound(String roundId) {
        listByRound(ReviewRound.get(roundId))
    }

    List<T> listByRound(ReviewRound round) {
        if (round.parentRound) {
            listByRoundResult(round.parentRound, round.parentPassNum)
        } else
            Achieve.where { reviewPlan == round.plan }.list()
    }

    int countByRound(ReviewRound round) {
        if (round.parentRound)
            round.parentPassNum
        else
            Achieve.where { reviewPlan == round.plan }.count()
    }

    List<T> listByRoundResult(ReviewRound round, int max) {
        AchieveRoundResult.findAllByRound(round, [max: max, sort: 'average', order: 'desc'])*.achieve
    }
}
