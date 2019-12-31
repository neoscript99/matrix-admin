package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.TopicMember
import org.springframework.stereotype.Service

@Service
class TopicMemberService extends AbstractService<TopicMember> {

    Number saveMembers(String topicId, List<String> memberIds) {
        deleteMatch([eq: [['topic.id', topicId]]])
        memberIds.each {
            save([topic: [id: topicId], member: [id: it]])
        }
        return memberIds.size()
    }

}
