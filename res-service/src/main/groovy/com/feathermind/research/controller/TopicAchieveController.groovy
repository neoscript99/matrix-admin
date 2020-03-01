package com.feathermind.research.controller

import com.feathermind.research.domain.res.AchieveTopic
import com.feathermind.research.service.AchieveService
import com.feathermind.research.service.TopicAchieveService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topicAchieve")
class TopicAchieveController extends AchieveController<AchieveTopic> {
    @Autowired
    TopicAchieveService topicAchieveService

    @Override
    AchieveService<AchieveTopic> getDomainService() {
        return topicAchieveService
    }
}
