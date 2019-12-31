package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.feathermind.research.domain.res.TopicWorkPlan

@RestController
@RequestMapping("/api/topicWorkPlan")
class TopicWorkPlanController extends DomainController<TopicWorkPlan> {
    @Autowired
    com.feathermind.research.service.TopicWorkPlanService topicWorkPlanService

    @PostMapping("/listStarted")
    ResponseEntity<List<TopicWorkPlan>> listStarted() {
        return ResponseEntity.ok(topicWorkPlanService.listStarted())
    }

    @Override
    AbstractService<TopicWorkPlan> getDomainService() {
        return topicWorkPlanService
    }
}
