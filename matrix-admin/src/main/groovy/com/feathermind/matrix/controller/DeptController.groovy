package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.DeptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/department")
class DeptController extends DomainController<Department> {
    @Autowired
    DeptService deptService

    @Override
    AbstractService<Department> getDomainService() {
        return deptService
    }
}
