package com.feathermind.research.controller

import com.feathermind.research.domain.res.AchievePaper
import com.feathermind.research.service.AchieveService
import com.feathermind.research.service.PaperService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/paper")
class PaperController extends AchieveController<AchievePaper> {
    @Autowired
    PaperService paperService

    @Override
    AchieveService<AchievePaper> getDomainService() {
        return paperService
    }
}
