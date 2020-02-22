package com.feathermind.research.service

import com.feathermind.matrix.service.AbstractService
import com.feathermind.research.domain.res.Paper
import org.springframework.stereotype.Service

@Service
class PaperService extends AbstractService<Paper> {
    @Override
    Paper save(Map map) {
        Paper entity = super.save(map)
        entity.paperFile.ownerId = entity.id
        entity.paperFile.ownerName = entity.title
        return entity;
    }
}
