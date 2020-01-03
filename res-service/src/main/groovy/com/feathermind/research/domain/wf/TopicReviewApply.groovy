package com.feathermind.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import com.feathermind.research.domain.res.ResUser
import grails.gorm.annotation.Entity
import com.feathermind.research.domain.res.ReviewPlan

/**
 * 课题成果评审申请
 * todo 是否必须为本系统内的课题，如果是的话“成果名称、立项编号、结题证书编号”都不用再次输入
 */
@Entity
class TopicReviewApply extends Apply {
    ReviewPlan plan
    //成果名称
    String achieveName
    //成果负责人姓名
    ResUser personInCharge

    //成果类别
    String achieveCateCode
    //立项情况
    String projectLevelCode
}
