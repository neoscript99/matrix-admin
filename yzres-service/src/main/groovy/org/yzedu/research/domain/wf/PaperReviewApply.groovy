package org.yzedu.research.domain.wf

import com.feathermind.matrix.domain.wf.Apply
import grails.gorm.annotation.Entity
import org.yzedu.research.domain.res.Paper
import org.yzedu.research.domain.res.ReviewPlan

/**
 * 论文评审申请
 * 初始状态为draft,查重通过后为pass
 */
@Entity
class PaperReviewApply extends Apply{
    ReviewPlan plan
    Paper paper

    static mapping = {
        paper fetch: 'join', lazy: false
    }
}
