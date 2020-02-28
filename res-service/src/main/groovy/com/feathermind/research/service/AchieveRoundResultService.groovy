package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.Achieve
import com.feathermind.research.domain.res.AchieveExpertScore
import com.feathermind.research.domain.res.AchieveRoundResult
import com.feathermind.research.domain.res.ReviewRound
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class AchieveRoundResultService extends AbstractService<AchieveRoundResult> {
    @Autowired
    ReviewRoundService reviewRoundService
    @Autowired
    AchieveExpertScoreService achieveExpertScoreService
    @Autowired
    ReviewRoundExpertService reviewRoundExpertService

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
        AchieveRoundResult.where { round == round }.deleteAll()
        int expTotal = reviewRoundExpertService.count([eq: [['round', round]]])
        Map<Achieve, List<AchieveExpertScore>> groupMap = achieveExpertScoreService.findByRound(round).groupBy {
            it.achieve
        }
        round.runStatus = 'success'
        round.runError = null
        groupMap.each { achieve, expertScoreList ->
            AchieveRoundResult result = new AchieveRoundResult(achieve: achieve, round: round, hasError: false)
            List<Integer> scores = expertScoreList.collect {
                def str = result.expertScores;
                def expStr = "${it.roundExpert.expert.name}: ${it.score}";
                result.expertScores = str ? str.concat(", ${expStr}") : expStr
                return it.score
            }
            if (expertScoreList.size() < expTotal) {
                result.hasError = true;
                result.message = '部分专家未评分'
                result.save()

                round.runError = '部分未评分'
                round.runStatus = 'warning'
                return
            }
            scores.sort()
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
        if (Achieve.where { reviewPlan == round.plan }.count() > groupMap.size()) {
            round.runError = '部分未评分'
            round.runStatus = 'warning'
        }
        round.save()
    }
}
