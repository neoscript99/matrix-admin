package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.Achieve
import com.feathermind.research.domain.res.AchieveRoundResult
import com.feathermind.research.domain.res.ReviewRound
import org.hibernate.sql.JoinType

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

    List<T> listByRoundResult(ReviewRound round, int num) {
        AchieveRoundResult.withCriteria {
            eq 'round', round
            achieve {
                // criteria关联查询默认都是inner join，需要手工设置outer join，前台参数传入也应该注意
                duplicateCheck(JoinType.LEFT_OUTER_JOIN.joinTypeValue) {
                    or {
                        isNull 'id'
                        eq 'success', true
                    }
                }
            }
            maxResults num
            order('average', 'desc')
        }*.achieve
    }
}
