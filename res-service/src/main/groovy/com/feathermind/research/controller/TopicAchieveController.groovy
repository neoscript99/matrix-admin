package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.TopicAchieve
import com.feathermind.research.service.TopicAchieveService
import com.feathermind.research.trait.ListByRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topicAchieve")
class TopicAchieveController extends DomainController<TopicAchieve>  implements ListByRole {
    @Autowired
    TopicAchieveService topicAchieveService

    @Override
    AbstractService<TopicAchieve> getDomainService() {
        return topicAchieveService
    }
}
