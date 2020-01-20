package com.feathermind.matrix.service

import com.feathermind.matrix.domain.wf.Apply
import org.springframework.stereotype.Service

@Service
class ApplyService extends AbstractService<Apply>{
    @Override
    Apply saveEntity(Apply entity) {
        //todo 记录日志
        return super.saveEntity(entity)
    }

    @Override
    Apply save(Map map) {
        //todo 记录日志
        return super.save(map)
    }
}
