package com.feathermind.matrix.security

import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.domain.sys.User
import groovy.transform.CompileStatic

@CompileStatic
class TokenDetails {
    String username
    //CAS、Auth2等第三方登录情况下，系统user可能为空
    User user
    Collection<String> authorities
    Collection<String> roles

    TokenDetails(String username, Collection<Role> roles) {
        this.username = username
        this.roles = roles*.roleCode

        authorities = new HashSet<String>()
        roles.each { role ->
            if (role.authorities)
                authorities.addAll(role.authorities.trim().split(','))
        }
    }

    TokenDetails(User user, Collection<Role> roles) {
        this(user.account, roles)
        this.user = user
    }

    /**
     * 用于jwt加密
     * @return
     */
    String getPassword() {
        user ? user.password : 'no_password'
    }
}
