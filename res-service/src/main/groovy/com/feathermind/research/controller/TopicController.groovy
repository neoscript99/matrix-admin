package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.wf.TopicFinishApply
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

    @PostMapping("/list")
    ResponseEntity<List<TopicFinishApply>> list(@RequestBody Map criteria) {
        def user = this.getSessionUser(true)
        def roles = this.getToken().roles.split(',')
        if (!roles.contains(MAIN_MANAGER.roleCode)) {
            if (roles.contains(DEPT_MANAGER.roleCode))
                criteria.eq = [['dept.id', user.dept.id]]
            else if (roles.contains(RES_USER.roleCode))
                criteria.eq = [['personInCharge', user]]
            else
                throw new RuntimeException('当前用户没有权限')
        }
        return ResponseEntity.ok(domainService.list(criteria))
    }

    @Override
    AbstractService<Topic> getDomainService() {
        return topicService
    }
}
