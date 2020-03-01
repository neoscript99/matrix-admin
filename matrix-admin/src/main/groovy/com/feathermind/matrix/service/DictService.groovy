package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.Dict
import org.springframework.stereotype.Service

@Service
class DictService extends AbstractService<Dict> {
    Dict getDict(String typeId, String code) {
        findFirst(eq: [['type.id', typeId], ['code', code]])
    }
}
