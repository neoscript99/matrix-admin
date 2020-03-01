package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.DictService
import com.feathermind.matrix.util.JsonUtil
import com.feathermind.research.domain.res.Achieve
import com.feathermind.research.domain.res.AchieveExpertScore
import com.feathermind.research.domain.res.AchieveRoundResult
import com.feathermind.research.domain.res.ReviewPlan
import com.feathermind.research.domain.res.ReviewRound
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic(TypeCheckingMode.SKIP)
class AchieveRoundResultService extends AbstractService<AchieveRoundResult> {
    @Autowired
    ReviewRoundService reviewRoundService
    @Autowired
    AchieveExpertScoreService achieveExpertScoreService
    @Autowired
    TopicAchieveService topicAchieveService
    @Autowired
    PaperService paperService
    @Autowired
    ReviewRoundExpertService reviewRoundExpertService
    @Autowired
    DictService dictService

    void calcRoundResult(String roundId) {
        def round = reviewRoundService.get(roundId)
        try {
            calcRoundResult(round)
        }
        catch (Throwable e) {
            log.error('评分轮次跑分错误：{}', e)
            round.runStatus = 'error'
            round.runError = e.message
            round.save()
        }
    }

    void calcRoundResult(ReviewRound round) {
        log.info("开始得分计算：{}", round)
        deleteByRound(round)
        int totalExperts = reviewRoundExpertService.count([eq: [['round', round]]])
        Map<Achieve, List<AchieveExpertScore>> groupMap = achieveExpertScoreService.findByRound(round).groupBy {
            it.achieve
        }
        round.runStatus = 'success'
        round.runError = null
        groupMap.each { achieve, expertScoreList ->
            AchieveRoundResult result = new AchieveRoundResult(achieve: achieve, round: round, hasError: false)
            expertScoreList.sort { a, b -> a.score <=> b.score }
            List<Map> infos = expertScoreList.collect {
                [name: it.roundExpert.expert.name, score: it.score]
            }
            result.scoresJson = JsonUtil.toJson(infos)
            List<Integer> scores = expertScoreList.collect { it.score }
            if (expertScoreList.size() < totalExperts) {
                result.hasError = true;
                round.runError = result.message = '部分专家未评分'
                result.save()

                round.runStatus = 'warning'
                return
            }
            if (round.avgAlgorithmCode == 'ignore-max-min') {
                if (scores.size() > 2) {
                    scores.remove(scores.size() - 1)
                    scores.remove(0)
                } else scores.clear()
            }
            int size = scores.size()
            result.average = size > 0
                    ? new BigDecimal((int) scores.sum()).setScale(2) / size
                    : BigDecimal.ZERO
            result.save()
        }
        def totalAchieves = getAchieveService(round.plan).countByRound(round)
        if (totalAchieves > groupMap.size()) {
            def type = dictService.getDict('res-review-type', round.plan.reviewTypeCode)
            round.runError = "部分未评分: 总${type.name}数${totalAchieves}, 已评分数${groupMap.size()}"
            round.runStatus = 'warning'
        }
        round.save()
    }

    def deleteByRound(ReviewRound r) {
        AchieveRoundResult.where { round == r }.deleteAll()
    }

    AchieveService getAchieveService(ReviewPlan plan) {
        plan.reviewTypeCode == 'paper' ? paperService : topicAchieveService
    }
}
