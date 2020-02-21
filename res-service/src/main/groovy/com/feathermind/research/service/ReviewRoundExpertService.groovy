package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.ReviewRound
import com.feathermind.research.domain.res.ReviewRoundExpert
import org.springframework.stereotype.Service

@Service
class ReviewRoundExpertService extends AbstractService<ReviewRoundExpert> {
    def saveRoundExpert(ReviewRound round, Iterable expertIds) {
        deleteMatch([eq: [['round', round]]])
        expertIds.eachWithIndex { def id, int idx ->
            save([round: round, expert: [id: id], seq: idx])
        }
    }
}
