package org.yzedu.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.matrix.service.AbstractService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yzedu.research.domain.res.YzDept
import org.yzedu.research.service.YzDeptService

@RestController
@RequestMapping("/api/yzDept")
class YzDeptController extends DomainController<YzDept> {
    @Autowired
    YzDeptService yzDeptService


    @Override
    AbstractService<YzDept> getDomainService() {
        return yzDeptService
    }
}
