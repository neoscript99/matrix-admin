package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.Topic
import com.feathermind.research.domain.res.TopicSupport
import org.springframework.stereotype.Service

@Service
class TopicSupportService extends AbstractService<TopicSupport> {

    void saveTopicSupports(Topic topic, List<Map> supports) {
        deleteMatch(eq: [['topic', topic]])
        supports.eachWithIndex { item, i ->
            new TopicSupport(topic: topic, support: [id: item.id], seq: i).save()
        }
    }

}
