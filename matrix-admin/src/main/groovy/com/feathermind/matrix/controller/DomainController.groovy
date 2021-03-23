package com.feathermind.matrix.controller


import com.feathermind.matrix.security.SecureController
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * SysWrite、SysRead两个为最大的权限，应该只赋值给管理员
 * @param <T >
 */
abstract class DomainController<T> extends SecureController {
    protected Logger log = LoggerFactory.getLogger(this.getClass())
    @Autowired
    UserService userService

    abstract AbstractService<T> getDomainService();

    /**
     * 默认get没有做权限限制，客户端正常拿不到id
     * @param req
     * @return
     */
    @PostMapping("/get")
    T get(@RequestBody Map req) {
        readOneAuthorize()
        return domainService.get(req.id)
    }

    @PostMapping("/delete")
    Number delete(@RequestBody Map req) {
        writeOneAuthorize()
        return domainService.deleteById(req.id)
    }

    @PostMapping("/deleteByIds")
    Number deleteByIds(@RequestBody Map req) {
        writeAuthorize()
        return domainService.deleteByIds(req.ids)
    }

    @PostMapping("/deleteMatch")
    Number deleteMatch(@RequestBody Map criteria) {
        writeAuthorize()
        return domainService.deleteMatch(criteria)
    }

    @PostMapping("/save")
    T save(@RequestBody Map entityMap) {
        writeOneAuthorize()
        return domainService.save(entityMap)
    }

    @PostMapping("/list")
    List<T> list(@RequestBody Map criteria) {
        readAuthorize()
        return domainService.list(preList(criteria))
    }

    @PostMapping("/count")
    Integer count(@RequestBody Map criteria) {
        readAuthorize()
        return domainService.count(preList(criteria))
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

}
