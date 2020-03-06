package com.feathermind.matrix.controller

import com.feathermind.matrix.controller.bean.ResBean
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
        def userList = SecurityContextHolder.getContext().getAuthentication().authorities
        if (userList.contains('SysAdmin')) return;
        if (!needOneList) return;
        def lookListFlat = needOneList.flatten();
        for (def it : lookListFlat) {
            if (userList.contains(it.toString())) return;
        }
        throw new RuntimeException(
                ResBean.json('authorize_fail', "当前用户无以下任一权限：${lookListFlat}"))
    }

    abstract String getName();

    abstract String getPackageName();


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
