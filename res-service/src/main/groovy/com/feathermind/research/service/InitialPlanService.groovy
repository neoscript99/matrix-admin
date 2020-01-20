package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import org.springframework.stereotype.Service
import com.feathermind.research.domain.res.InitialPlan

import java.time.LocalDate
import java.time.LocalDateTime

@Service
class InitialPlanService extends AbstractService<InitialPlan> {

    List listStarted() {
        def today = LocalDate.now().toString()
        def hour = LocalDateTime.now().hour
        def list = InitialPlan.findAll(sort: 'planBeginDay') {
            planBeginDay <= today && planEndDay >= today
        }

        return list.findAll {
            //planEndDay控制到15点截止
            !(hour >= 15 && it.planEndDay == today)
        }
    }
}
