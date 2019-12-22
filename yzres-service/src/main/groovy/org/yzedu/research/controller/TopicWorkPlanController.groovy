package org.yzedu.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yzedu.research.domain.res.TopicWorkPlan
import org.yzedu.research.service.TopicWorkPlanService

@RestController
@RequestMapping("/api/topicWorkPlan")
class TopicWorkPlanController extends DomainController<TopicWorkPlan> {
    @Autowired
    TopicWorkPlanService topicWorkPlanService

    @PostMapping("/listStarted")
    ResponseEntity<List<TopicWorkPlan>> listStarted() {
        return ResponseEntity.ok(topicWorkPlanService.listStarted())
    }

    @Override
    AbstractService<TopicWorkPlan> getDomainService() {
        return topicWorkPlanService
    }
}
