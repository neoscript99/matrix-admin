package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.TopicSupport
import com.feathermind.research.service.TopicSupportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topicSupport")
class TopicSupportController extends DomainController<TopicSupport> {
    @Autowired
    TopicSupportService topicSupportService

    @Override
    TopicSupportService getDomainService() {
        return topicSupportService
    }
}
