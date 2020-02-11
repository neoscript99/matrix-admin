package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService

import java.time.LocalDate

class PlanService<T> extends AbstractService<T> {
    List listStarted() {
        def today = LocalDate.now().toString()
        def list = list([le: [['planBeginDay', today]], ge: [['planEndDay', today]], order: ['planBeginDay']])
        return list.findAll {
            it.planStatusCode == 'going'
        }
    }
}
