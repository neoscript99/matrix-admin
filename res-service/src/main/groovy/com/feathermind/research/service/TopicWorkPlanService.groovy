package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import org.springframework.stereotype.Service
import com.feathermind.research.domain.res.TopicWorkPlan

import java.time.LocalDate

@Service
class TopicWorkPlanService extends AbstractService<TopicWorkPlan> {

    List listStarted() {
        def today = LocalDate.now().toString()
        return TopicWorkPlan.findAll(sort: 'planBeginDay') {
            planBeginDay <= today && planEndDay >= today
        }
    }
}
