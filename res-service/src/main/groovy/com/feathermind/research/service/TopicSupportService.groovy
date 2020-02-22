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
            def ts = saveEntity(new TopicSupport(topic: topic, support: [id: item.id], seq: i))
            ts.support.ownerId = ts.id
            ts.support.ownerName = ts.topic.topicName
        }
    }

}
