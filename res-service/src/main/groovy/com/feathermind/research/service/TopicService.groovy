package com.feathermind.research.service

import cn.hutool.core.util.StrUtil
import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.InitialPlan
import com.feathermind.research.domain.res.ResDept
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.stereotype.Service
import com.feathermind.research.domain.res.Topic

import java.time.LocalDate

@Service
@CompileStatic(TypeCheckingMode.SKIP)
class TopicService extends AbstractService<Topic> {
    QualificationCheckResult checkQualification(InitialPlan plan, ResDept dept) {
        def res = new QualificationCheckResult()
        def planLimit = (plan.maxNumberPerDept && plan.maxNumberPerDept < dept.maxApplyNum)
        int maxNum = planLimit ? plan.maxNumberPerDept : dept.maxApplyNum
        def today = LocalDate.now().toString()
        List notFinishList = Topic.where {
            initialPlan != plan &&
                    initialPlan.planCateCode == plan.planCateCode &&
                    prepareFinishDay < today &&
                    (topicStatusCode == 'applied' ||
                            (topicStatusCode == 'created' && initialApply.statusCode != 'fail')
                    )
        }.list()
        int notFinishNum = notFinishList.size()
        //同计划、同单位，状态不是fail的申请数量
        int currNum = count(eq: [['initialPlan', plan], ['dept', dept]], initialApply: [ne: [['statusCode', 'fail']]])
        if (currNum + notFinishNum >= maxNum) {
            res.success = false;
            String notInfo = notFinishNum > 0 ? " + 超期课题数${notFinishNum}".toString() : ''
            res.reasons << "贵单位当前计划申请数${currNum}${notInfo}, 已达到${planLimit ? '本计划' : '本单位'}限制数量${maxNum}".toString()
            if (notFinishNum > 0) {
                res.reasons << "  同类别的计划中存在${notFinishNum}个超期未结题的课题，请及时处理".toString()
                notFinishList.eachWithIndex { item, i ->
                    res.reasons << "  超期课题${i + 1}：${item.topicName}(计划完成时间：${item.prepareFinishDay})".toString()
                }
            }
        }
        return res
    }

    @Override
    Topic save(Map map) {
        Topic topic = super.save(map)
        if (map.initialReport) {
            topic.initialReport.ownerId = topic.id
            topic.initialReport.ownerName = topic.topicName
        }
        if (map.topicStatusCode == 'applied' && StrUtil.isEmpty(topic.initialCode)) {
            topic.initialCode = genNextInitialCode(topic)
        }
        return topic
    }

    String genNextInitialCode(Topic topic) {
        String prefix = "${topic.topicCateCode}${topic.initialPlan.planYear}"
        String max = Topic.createCriteria().get {
            like 'initialCode', "${prefix}%".toString()
            projections {
                max('initialCode')
            }
        }
        def num = max ? Integer.valueOf(max.replaceFirst(prefix, '')) + 1 : 1
        return "${prefix}${StrUtil.padPre(num.toString(), 3, '0')}".toString()
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
