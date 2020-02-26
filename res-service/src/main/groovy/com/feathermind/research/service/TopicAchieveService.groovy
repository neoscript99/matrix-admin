package com.feathermind.research.service

import com.feathermind.research.domain.res.AchieveTopic
import org.springframework.stereotype.Service

@Service
class TopicAchieveService extends AchieveService<AchieveTopic> {
    @Override
    AchieveTopic save(Map map) {
        //如果当前是修改，需要把原课题恢复为结题状态，先不管是否选择了不同的课题
        if (map.id)
            restoreTopicStatus(map.id)
        AchieveTopic achieve = super.save(map)
        //当前课题的状态改为“已参评”
        achieve.topic.topicStatusCode = 'reviewed'
        achieve.summary.ownerId = achieve.id
        achieve.summary.ownerName = achieve.name
        achieve.mainReport.ownerId = achieve.id
        achieve.mainReport.ownerName = achieve.name
        return achieve
    }

    @Override
    Number deleteById(Object id) {
        restoreTopicStatus(id)
        return super.deleteById(id)
    }

    @Override
    Number deleteByIds(List idList) {
        idList.each { restoreTopicStatus(it) }
        return super.deleteByIds(idList)
    }
    /**
     * 课题恢复为结题状态
     * @param topicAchieveId
     * @return
     */
    def restoreTopicStatus(String topicAchieveId) {
        get(topicAchieveId).topic.topicStatusCode = 'finished';
    }
}
