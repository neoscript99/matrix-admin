package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.feathermind.research.domain.res.TopicWorkPlan
import com.feathermind.research.domain.wf.TopicInitialApply
import com.feathermind.research.service.TopicInitialApplyService
import static com.feathermind.research.config.common.InitEntity.*

@RestController
@RequestMapping("/api/topicInitialApply")
class TopicInitialApplyController extends DomainController<TopicInitialApply> {
    @Autowired
    TopicInitialApplyService topicInitialApplyService

    @PostMapping("/list")
    ResponseEntity<List<TopicInitialApply>> list(@RequestBody Map criteria) {
        def user = this.getSessionUser(true)
        def roles = this.getToken().roles.split(',')
        if (!roles.contains(MAIN_MANAGER.roleCode)) {
            if (roles.contains(DEPT_MANAGER.roleCode))
                criteria.topic = [eq: [['dept.id', user.dept.id]]]
            else if (roles.contains(RES_USER.roleCode))
                criteria.eq = [['applier', user]]
        }
        return ResponseEntity.ok(domainService.list(criteria))
    }


    @Override
    AbstractService<TopicWorkPlan> getDomainService() {
        return topicInitialApplyService
    }
}
