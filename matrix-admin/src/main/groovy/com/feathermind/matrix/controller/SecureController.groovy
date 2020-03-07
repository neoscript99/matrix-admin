package com.feathermind.matrix.controller

import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.security.TokenDetails
import groovy.transform.CompileStatic
import org.springframework.security.core.context.SecurityContextHolder

/**
 * SysAdmin、SysWrite、SysRead为最大的权限，应该只赋值给管理员
 */
@CompileStatic
abstract class SecureController {
    /**
     * 检查权限，当前用户是否具有其中一个权限
     * @param needOneList 多个List<String>或String
     *
     * 如果没有权限，抛出异常，代表非法访问
     */
    void authorize(Object... needOneList) {
        def userList = tokenDetails.plainAuthorities
        if (userList.contains('SysAdmin')) return;
        if (!needOneList) return;
        def lookListFlat = needOneList.flatten();
        for (def lookAuthority : lookListFlat) {
            if (userList.contains(lookAuthority.toString())) return;
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        def trace = stackTrace.find {
            def className = it.className
            !(className.startsWith('java.')
                    || className.startsWith('org.codehaus.groovy')
                    || className.startsWith('sun.reflect')
                    || className.indexOf('\$') > -1
                    || className.indexOf('SecureController') > -1)
        }
        throw new RuntimeException(
                ResBean.json('AuthorizeFail', "当前用户（${userList}）无以下任一权限：SysAdmin, ${lookListFlat} at ${trace}"))
    }

    protected User getCurrentUser() {
        getCurrentUser(true)
    }

    protected User getCurrentUser(boolean isNeed) {
        def user = tokenDetails.user
        if (user)
            return user
        else if (isNeed)
            throw new RuntimeException(ResBean.json('NoUser', '用户未登录'))
    }

    protected TokenDetails getTokenDetails() {
        def principal = SecurityContextHolder.getContext().getAuthentication().principal
        if (principal instanceof TokenDetails)
            return (TokenDetails) principal
        else
            throw new RuntimeException(ResBean.json('IllegalToken', "非法token：$principal"))
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
