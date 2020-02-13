package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.service.InitialPlanService
import com.feathermind.research.service.QualificationCheckResult
import com.feathermind.research.service.ResDeptService
import com.feathermind.research.trait.ListByRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.feathermind.research.domain.res.Topic
import com.feathermind.research.service.TopicService

@RestController
@RequestMapping("/api/topic")
class TopicController extends DomainController<Topic> implements ListByRole {
    @Autowired
    TopicService topicService
    @Autowired
    InitialPlanService initialPlanService
    @Autowired
    ResDeptService resDeptService

    @PostMapping("/checkQualification")
    ResponseEntity<QualificationCheckResult> checkQualification(@RequestBody Map req) {
        def initialPlan = initialPlanService.get(req.initialPlanId)
        def dept = resDeptService.get(req.deptId)
        return ResponseEntity.ok(topicService.checkQualification(initialPlan, dept))
    }

    @Override
    AbstractService<Topic> getDomainService() {
        return topicService
    }
}
