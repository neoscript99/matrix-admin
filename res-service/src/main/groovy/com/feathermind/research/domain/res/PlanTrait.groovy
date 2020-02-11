package com.feathermind.research.domain.res

import com.feathermind.matrix.trait.AutoTime

import java.time.LocalDate
import java.time.LocalDateTime

trait PlanTrait extends AutoTime {
    abstract String getPlanBeginDay();

    abstract String getPlanEndDay();
    /**
     * 计划状态
     * @return
     */
    String getPlanStatusCode() {
        def today = LocalDate.now().toString()
        def hour = LocalDateTime.now().hour
        if (planBeginDay > today)
            return 'before'
        else if (planEndDay < today)
            return 'done'
        //planEndDay控制到15点截止
        else if (planEndDay == today && hour >= 15)
            return 'done'
        else return 'going'
    }
}
