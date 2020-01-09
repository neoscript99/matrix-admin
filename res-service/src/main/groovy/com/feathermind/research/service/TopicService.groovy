package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.InitialPlan
import com.feathermind.research.domain.res.ResDept
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.stereotype.Service
import com.feathermind.research.domain.res.Topic

@Service
@CompileStatic(TypeCheckingMode.SKIP)
class TopicService extends AbstractService<Topic> {
    QualificationCheckResult checkQualification(InitialPlan plan, ResDept dept) {
        def res = new QualificationCheckResult()
        def planLimit = (plan.maxNumberPerDept && plan.maxNumberPerDept < dept.maxApplyNum)
        int maxNum = planLimit ? plan.maxNumberPerDept : dept.maxApplyNum
        //同计划、同单位，状态不是fail的申请数量
        int currNum = count(eq: [['initialPlan', plan], ['dept', dept]], initialApply: [ne: [['statusCode', 'fail']]])
        if (currNum >= maxNum) {
            res.success = false;
            res.reasons << "贵单位的申请数量已达到${planLimit ? '本计划' : '本单位'}限制数量".toString()
        }

        int notFinishNum = Topic.where {
            topicCateCode == plan.topicCateCode && initialPlan != plan &&
                    (topicStatusCode == 'applied' ||
                            (topicStatusCode == 'created' && initialApply.statusCode != 'fail')
                    )
        }.count()
        if (notFinishNum > 0) {
            res.success = false;
            res.reasons << '同类别的计划中存在未结题的课题，请及时处理'
        }
        return res
    }
}


class QualificationCheckResult {
    QualificationCheckResult() {
        success = true
        reasons = []
    }
    Boolean success
    List<String> reasons
}
