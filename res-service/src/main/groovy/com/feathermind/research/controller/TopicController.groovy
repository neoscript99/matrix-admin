package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.service.InitialPlanService
import com.feathermind.research.service.QualificationCheckResult
import com.feathermind.research.service.ResDeptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.feathermind.research.domain.res.Topic
import com.feathermind.research.service.TopicService

import static com.feathermind.research.config.common.InitEntity.DEPT_MANAGER
import static com.feathermind.research.config.common.InitEntity.MAIN_MANAGER
import static com.feathermind.research.config.common.InitEntity.RES_USER

@RestController
@RequestMapping("/api/topic")
class TopicController extends DomainController<Topic> {
    @Autowired
    TopicService topicService
    @Autowired
    InitialPlanService initialPlanService
    @Autowired
    ResDeptService resDeptService

    @PostMapping("/list")
    ResponseEntity<List<Topic>> list(@RequestBody Map criteria) {
        def user = this.getSessionUser(true)
        def roles = this.getToken().roles.split(',')
        if (!roles.contains(MAIN_MANAGER.roleCode)) {
            if (!criteria.eq)
                criteria.eq = []
            if (roles.contains(DEPT_MANAGER.roleCode))
                criteria.eq << ['dept.id', user.dept.id]
            else if (roles.contains(RES_USER.roleCode))
                criteria.eq << ['personInCharge', user]
            else
                throw new RuntimeException('当前用户没有权限')
        }
        return ResponseEntity.ok(domainService.list(criteria))
    }

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
