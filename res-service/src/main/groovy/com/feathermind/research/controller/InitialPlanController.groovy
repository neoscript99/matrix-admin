package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.service.InitialPlanService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.feathermind.research.domain.res.InitialPlan

@RestController
@RequestMapping("/api/initialPlan")
class InitialPlanController extends DomainController<InitialPlan> {
    @Autowired
    InitialPlanService initialPlanService

    @PostMapping("/listStarted")
    ResponseEntity<List<InitialPlan>> listStarted() {
        return ResponseEntity.ok(initialPlanService.listStarted())
    }

    @Override
    AbstractService<InitialPlan> getDomainService() {
        return initialPlanService
    }
}
