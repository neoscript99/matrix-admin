package com.feathermind.research.service

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.util.DateUtil
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

    /**
     * 返回对应专家，当前未截止的评分轮次
     * @param expert
     * @return
     */
    List<ReviewRoundExpert> listByExpert(User expert) {
        list([eq: [['expert.id', expert.id]], round: [ge: [['endDay', DateUtil.dayStr()]]]])
    }
}
