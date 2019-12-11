package org.yzedu.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import org.yzedu.research.domain.res.ReviewPlan
import org.yzedu.research.domain.res.Topic

/**
 * 课题成果评审申请
 * todo 是否必须为本系统内的课题，如果是的话“成果名称、立项编号、结题证书编号”都不用再次输入
 */
@Entity
class TopicReviewApply extends Apply {
    ReviewPlan plan
    Topic topic
    String achieveCateCode
    String projectLevelCode

    static mapping = {
        topic fetch: 'join', lazy: false
    }
}
