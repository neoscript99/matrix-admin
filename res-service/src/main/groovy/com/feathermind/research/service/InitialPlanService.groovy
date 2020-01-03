package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import org.springframework.stereotype.Service
import com.feathermind.research.domain.res.InitialPlan

import java.time.LocalDate

@Service
class InitialPlanService extends AbstractService<InitialPlan> {

    List listStarted() {
        def today = LocalDate.now().toString()
        return InitialPlan.findAll(sort: 'planBeginDay') {
            planBeginDay <= today && planEndDay >= today
        }
    }
}
