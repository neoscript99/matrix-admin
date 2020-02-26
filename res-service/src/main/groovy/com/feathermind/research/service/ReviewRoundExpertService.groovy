package com.feathermind.research.service

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.util.DateUtil
import com.feathermind.research.domain.res.ReviewRound
import com.feathermind.research.domain.res.ReviewRoundExpert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewRoundExpertService extends AbstractService<ReviewRoundExpert> {
    @Autowired
    AchieveExpertScoreService achieveExpertScoreService
    /**
     * 遍历原专家列表，如果新列表中存在则更新排序号
     * 如果不存在，删除原实体，并删除该专家在本轮的所有打分信息
     *
     * @param round
     * @param expertIds
     * @return
     */
    def saveRoundExpert(ReviewRound round, Iterable expertIds) {
        def oldList = list([eq: [['round', round]]])
        expertIds.eachWithIndex { def expertId, int idx ->
            def oldEnity = oldList.find { it.roundId == round.id && it.expertId == expertId }
            if (oldEnity) {
                oldEnity.seq = idx
                oldList.remove(oldEnity)
            } else
                save([round: round, expert: [id: expertId], seq: idx])
        }
        oldList.each {
            achieveExpertScoreService.deleteMatch([eq: [['roundExpert.id', it.id]]])
            it.delete()
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
