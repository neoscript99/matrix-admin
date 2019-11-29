package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@CrossOrigin
abstract class DomainController<T> {
    protected Logger log = LoggerFactory.getLogger(this.getClass())
    private Class<T> domain;

    public DomainController() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        domain = (Class) params[0];
    }

    abstract AbstractService<T> getDomainService();


    @PostMapping("/get")
    ResponseEntity<T> get(@RequestBody String id) {
        return ResponseEntity.ok(domainService.get(id))
    }

    @PostMapping("/delete")
    ResponseEntity<Number> delete(@RequestBody String id) {
        return ResponseEntity.ok(domainService.deleteById(id))
    }

    @PostMapping("/save")
    ResponseEntity<T> save(@RequestBody Map entityMap) {
        return ResponseEntity.ok(domainService.save(entityMap))
    }
}
