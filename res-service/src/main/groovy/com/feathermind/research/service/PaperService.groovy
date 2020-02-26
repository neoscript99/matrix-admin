package com.feathermind.research.service

import com.feathermind.research.domain.res.AchievePaper
import org.springframework.stereotype.Service

@Service
class PaperService extends AchieveService<AchievePaper> {
    @Override
    AchievePaper save(Map map) {
        AchievePaper entity = super.save(map)
        entity.paperFile.ownerId = entity.id
        entity.paperFile.ownerName = entity.name
        return entity;
    }
}
