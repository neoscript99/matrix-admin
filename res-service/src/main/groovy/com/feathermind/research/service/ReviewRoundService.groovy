package com.feathermind.research.service

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.ReviewRound
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewRoundService extends AbstractService<ReviewRound> {
    @Autowired
    ResUserService resUserService
    @Autowired
    ReviewRoundExpertService reviewRoundExpertService
    @Autowired
    AchieveReviewScoreService reviewRoundScoreService

    @Override
    ReviewRound save(Map map) {
        def round = super.save(map)
        reviewRoundExpertService.saveRoundExpert(round, map.experts)
        def pass = User.encodePassword(round.expertPassword)
        resUserService.updateMatch([inList: [['id', map.experts]]], [password: pass])
        return round
    }

    @Override
    Number deleteByIds(List idList) {
        idList.each {
            def roundMap = [eq: [['round.id', it]]]
            reviewRoundScoreService.deleteMatch([roundExpert: roundMap])
            reviewRoundExpertService.deleteMatch(roundMap)
        }
        return super.deleteByIds(idList)
    }
}
