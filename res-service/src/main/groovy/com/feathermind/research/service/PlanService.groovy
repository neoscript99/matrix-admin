package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService

import java.time.LocalDate
import java.time.LocalDateTime

class PlanService<T> extends AbstractService<T> {
    List listStarted() {
        def today = LocalDate.now().toString()
        def hour = LocalDateTime.now().hour
        def list = list([le: [['planBeginDay', today]], ge: [['planEndDay', today]], order: ['planBeginDay']])
        return list.findAll {
            //planEndDay控制到15点截止
            !(hour >= 15 && it.planEndDay == today)
        }
    }
}
