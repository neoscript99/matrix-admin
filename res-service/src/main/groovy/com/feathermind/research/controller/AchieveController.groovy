package com.feathermind.research.controller

import com.feathermind.matrix.controller.DomainController
import com.feathermind.research.domain.res.Achieve
import com.feathermind.research.service.AchieveService
import com.feathermind.research.trait.ListByRole
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

abstract class AchieveController<T extends Achieve> extends DomainController<T> implements ListByRole {

    @PostMapping("/listByRound")
    ResponseEntity<List<T>> listByRound(@RequestBody Map req) {
        return ResponseEntity.ok(domainService.listByRound(req.roundId))
    }

    @Override
    abstract AchieveService<T> getDomainService()
}
