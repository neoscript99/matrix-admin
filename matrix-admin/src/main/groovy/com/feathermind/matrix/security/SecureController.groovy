package com.feathermind.matrix.security

import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.util.MatrixException
import com.sun.org.apache.bcel.internal.generic.NEW
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.core.MethodParameter
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingPathVariableException

/**
 * SysAdmin、SysWrite、SysRead为最大的权限，应该只赋值给管理员
 */
@CompileStatic
@Slf4j
abstract class SecureController {
    /**
     * 检查权限，当前用户是否具有其中一个权限
     * @param needOneList 多个List<String>或String
     *
     * 如果没有权限，抛出异常，代表非法访问
     */
    void authorize(Object... needOneList) {
        if (!needOneList) return;
        def userList = tokenDetails.authorities
        if (userList.contains('SysAdmin')) return;
        def lookListFlat = needOneList.flatten();
        for (def lookAuthority : lookListFlat) {
            if (userList.contains(lookAuthority.toString())) return;
        }
        def e = new MatrixException('AuthorizeFail', "用户(${tokenDetails.username})无此功能权限")
        log.error("用户权限: ${userList}，需要其中之一：${lookListFlat}或SysAdmin".toString())
        throw e
    }

    protected User getCurrentUser() {
        getCurrentUser(true)
    }

    protected User getCurrentUser(boolean isNeed) {
        def user = tokenDetails?.user
        if (user)
            return user
        else if (isNeed)
            throw new MatrixException('NoUser', '用户未登录')
    }

    protected TokenDetails getTokenDetails() {
        def tokenDetails = TokenHolder.getToken()
        if (!tokenDetails)
            throw new MatrixException('NoToken', "登录信息不存在，无法执行")
        return tokenDetails;
    }

    String getName() { return null }

    String getPackageName() { return null }


    List getReadAuthorities() {
        String name = this.name
        String pName = packageName
        ['SysRead', "${pName}All", "${pName}Read", "${name}All", "${name}Read"]
    }

    List getWriteAuthorities() {
        String name = this.name
        String pName = packageName
        ['SysWrite', "${pName}All", "${pName}Write", "${name}All", "${name}Write"]
    }

    void readAuthorize() {
        authorize(readAuthorities)
    }

    void readOneAuthorize() {
        authorize(readAuthorities, "${name}ReadOne")
    }

    void writeAuthorize() {
        authorize(writeAuthorities)
    }

    void writeOneAuthorize() {
        authorize(writeAuthorities, "${name}WriteOne")
    }
}
