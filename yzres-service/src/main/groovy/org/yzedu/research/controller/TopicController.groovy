package org.yzedu.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yzedu.research.domain.res.Topic
import org.yzedu.research.service.TopicService

@RestController
@RequestMapping("/api/topic")
class TopicController extends DomainController<Topic> {
    @Autowired
    TopicService topicService


    @Override
    AbstractService<Topic> getDomainService() {
        return topicService
    }
}
