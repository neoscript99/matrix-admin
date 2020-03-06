package com.feathermind.matrix.controller

import com.feathermind.matrix.controller.bean.ResBean
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

/**
 * SysWrite、SysRead两个为最大的权限，应该只赋值给管理员
 * @param <T >
 */
@CrossOrigin(origins = ["http://localhost:3000", "null"], allowCredentials = "true")
abstract class DomainController<T> extends SecureController {
    protected Logger log = LoggerFactory.getLogger(this.getClass())
    @Autowired(required = false)
    GormSessionBean gormSessionBean
    @Autowired
    UserService userService

    abstract AbstractService<T> getDomainService();

    /**
     * 默认get没有做权限限制，客户端正常拿不到id
     * @param req
     * @return
     */
    @PostMapping("/get")
    ResponseEntity<T> get(@RequestBody Map req) {
        readOneAuthorize()
        return ResponseEntity.ok(domainService.get(req.id))
    }

    @PostMapping("/delete")
    ResponseEntity<Number> delete(@RequestBody Map req) {
        writeOneAuthorize()
        return ResponseEntity.ok(domainService.deleteById(req.id))
    }

    @PostMapping("/deleteByIds")
    ResponseEntity<Number> deleteByIds(@RequestBody Map req) {
        writeAuthorize()
        return ResponseEntity.ok(domainService.deleteByIds(req.ids))
    }

    @PostMapping("/deleteMatch")
    ResponseEntity<Number> deleteMatch(@RequestBody Map criteria) {
        writeAuthorize()
        return ResponseEntity.ok(domainService.deleteMatch(criteria))
    }

    @PostMapping("/save")
    ResponseEntity<T> save(@RequestBody Map entityMap) {
        writeOneAuthorize()
        return ResponseEntity.ok(domainService.save(entityMap))
    }

    @PostMapping("/list")
    ResponseEntity<List<T>> list(@RequestBody Map criteria) {
        readAuthorize()
        return ResponseEntity.ok(domainService.list(preList(criteria)))
    }

    @PostMapping("/count")
    ResponseEntity<Integer> count(@RequestBody Map criteria) {
        readAuthorize()
        return ResponseEntity.ok(domainService.count(preList(criteria)))
    }

    Map preList(Map criteria) {
        return criteria
    }

    String getName() {
        return this.domainService.domain.simpleName
    }

    String getPackageName() {
        String domainPackage = this.domainService.domain.package
        if (!domainPackage || domainPackage.lastIndexOf('.') < 0) return 'defaultPackage'
        // domain类的最里层包名是表名前缀，可以做一定规划，做权限控制
        String last = domainPackage.substring(domainPackage.lastIndexOf('.') + 1)
        return "${last}Package"
    }

    protected User getSessionUser(boolean isNeed = false) {
        def token = gormSessionBean.token
        def user = userService.findByAccount(token.username)
        if (user)
            return user
        else if (isNeed)
            throw new RuntimeException(ResBean.json('not_login', '用户未登录'))
    }

    protected Token getToken() {
        if (gormSessionBean)
            return gormSessionBean.token
    }
}
