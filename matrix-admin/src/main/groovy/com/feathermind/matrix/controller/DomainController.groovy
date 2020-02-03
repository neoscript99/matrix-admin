package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.Token
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@CrossOrigin(origins = ["http://localhost:3000", "null"], allowCredentials = "true")
abstract class DomainController<T> {
    protected Logger log = LoggerFactory.getLogger(this.getClass())
    private Class<T> domain;
    @Autowired(required = false)
    GormSessionBean gormSessionBean
    @Autowired
    UserService userService

    public DomainController() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        domain = (Class) params[0];
    }

    abstract AbstractService<T> getDomainService();


    @PostMapping("/get")
    ResponseEntity<T> get(@RequestBody Map req) {
        return ResponseEntity.ok(domainService.get(req.id))
    }

    @PostMapping("/delete")
    ResponseEntity<Number> delete(@RequestBody Map req) {
        return ResponseEntity.ok(domainService.deleteById(req.id))
    }

    @PostMapping("/deleteByIds")
    ResponseEntity<Number> deleteByIds(@RequestBody Map req) {
        return ResponseEntity.ok(domainService.deleteByIds(req.ids))
    }

    @PostMapping("/deleteMatch")
    ResponseEntity<Number> deleteMatch(@RequestBody Map criteria) {
        return ResponseEntity.ok(domainService.deleteMatch(criteria))
    }

    @PostMapping("/save")
    ResponseEntity<T> save(@RequestBody Map entityMap) {
        return ResponseEntity.ok(domainService.save(entityMap))
    }

    @PostMapping("/list")
    ResponseEntity<List<T>> list(@RequestBody Map criteria) {
        return ResponseEntity.ok(domainService.list(preList(criteria)))
    }

    @PostMapping("/count")
    ResponseEntity<Integer> count(@RequestBody Map criteria) {
        return ResponseEntity.ok(domainService.count(preList(criteria)))
    }

    Map preList(Map criteria) {
        return criteria
    }

    protected User getSessionUser(boolean isNeed = false) {
        def token = gormSessionBean.token
        def user = userService.findByAccount(token.username)
        if (user)
            return user
        else if (isNeed)
            throw new RuntimeException('用户未登录')
    }

    protected Token getToken() {
        if (gormSessionBean)
            return gormSessionBean.token
    }
}
