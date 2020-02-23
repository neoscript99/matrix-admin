package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import grails.gorm.annotation.Entity

/**
 * 论文基本信息
 */
@Entity
class AchievePaper extends Achieve{
    AttachmentInfo paperFile


    static mapping = {
        paperFile fetch: 'join', lazy: false
    }
}
