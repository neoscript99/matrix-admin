package com.feathermind.research.controller;

import com.feathermind.matrix.controller.DomainController;
import com.feathermind.research.domain.res.DuplicateCheckResult;
import com.feathermind.research.service.DuplicateCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/duplicateCheck")
class DuplicateCheckController extends DomainController<DuplicateCheckResult> {
    @Autowired
    DuplicateCheckService duplicateCheckService;

    @Override
    public DuplicateCheckService getDomainService() {
        return duplicateCheckService;
    }
}
