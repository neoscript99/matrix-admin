package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import grails.gorm.annotation.Entity

/**
 * 课题成果
 * “成果名称、负责人、团队成员”都可能和课题不一样
 * 团队成员
 */
@Entity
class AchieveTopic extends Achieve{
    //课题组成员, 0-4人
    String members
    //成果类别
    String achieveCateCode
    //立项情况
    String projectLevelCode
    //成果简述（内容提要）
    //说明：限300字，分三点简述：为什么做该课题（约20%字数），怎么在做（约50%字数），效果怎样（约30%字数）。
    AttachmentInfo summary
    //上传成果主报告盲评文本
    AttachmentInfo mainReport

    Topic topic

    static mapping = {
        summary fetch: 'join', lazy: false
        mainReport fetch: 'join', lazy: false
        topic fetch: 'join', lazy: false
    }
    static constraints = {
        members nullable: true, maxSize: 512
    }
}
