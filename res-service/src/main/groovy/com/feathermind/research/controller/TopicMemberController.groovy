package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.TopicMember
import com.feathermind.research.service.TopicMemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topicMember")
class TopicMemberController extends DomainController<TopicMember> {
    @Autowired
    TopicMemberService topicMemberService

    /**
     * 保存成员信息
     * @param req{ topicId: string, memberIds: string[] }* @return
     */
    @PostMapping("/saveMembers")
    ResponseEntity<Number> saveMembers(@RequestBody Map req) {
        return ResponseEntity.ok(domainService.saveMembers(req.topicId, req.memberIds))
    }


    @Override
    TopicMemberService getDomainService() {
        return topicMemberService
    }
}
