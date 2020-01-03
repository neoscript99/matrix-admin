package com.feathermind.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import com.feathermind.research.domain.res.ReviewPlan

/**
 * 论文评审申请
 * 初始状态为draft,查重通过后为pass
 */
@Entity
class PaperReviewApply extends Apply{
    ReviewPlan plan
    /*查重系统*/
    String duplicateCheckResult
    String duplicateCheckDate
    String duplicateCheckMemo
}
