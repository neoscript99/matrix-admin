package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.domain.wf.Apply
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ApplyService extends AbstractService<Apply> {
    @Autowired
    ApplyLogService applyLogService

    Apply saveWithLog(Map applyMap, User operator, String info) {
        String fromCode;
        if (applyMap.id) {
            def oldApply = get(applyMap.id)
            fromCode = oldApply.statusCode
            oldApply.discard()
        }
        def newApply = this.save(applyMap)
        applyLogService.save(apply: newApply, operator: operator, info: info,
                fromStatusCode: fromCode, toStatusCode: newApply.statusCode)
        return newApply;
    }

    Apply updateStatus(Apply oldApply, String toStatusCode, User operator, String info) {
        String fromStatusCode = oldApply.statusCode;
        applyLogService.save(apply: oldApply, operator: operator, info: info,
                fromStatusCode: fromStatusCode, toStatusCode: toStatusCode)
        oldApply.statusCode = toStatusCode
        oldApply.merge()
    }
}
