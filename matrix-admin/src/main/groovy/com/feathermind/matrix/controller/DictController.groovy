package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Dict
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.DictService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dict")
class DictController extends DomainController<Dict> {
    @Autowired
    DictService dictService

    @Override
    AbstractService<Dict> getDomainService() {
        return dictService
    }
}
