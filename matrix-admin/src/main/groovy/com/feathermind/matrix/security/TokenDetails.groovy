package com.feathermind.matrix.security

import com.feathermind.matrix.domain.sys.Role
import com.feathermind.matrix.domain.sys.User
import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@CompileStatic
class TokenDetails implements UserDetails {
    String username
    //CAS、Auth2等第三方登录情况下，系统user可能为空
    User user
    Collection<GrantedAuthority> authorities
    Collection<String> roles

    TokenDetails(String username, Collection<Role> roles) {
        this.username = username
        this.roles = roles*.roleCode

        authorities = new HashSet<GrantedAuthority>()
        roles.each { role ->
            if (role.authorities)
                role.authorities.trim().split(',')
                        .each { authorities << new SimpleGrantedAuthority(it) }
        }
    }

    TokenDetails(User user, Collection<Role> roles) {
        this(user.account, roles)
        this.user = user
    }

    List<String> getPlainAuthorities() {
        authorities.collect { it.authority }
    }

    @Override
    String getPassword() {
        user ? user.password : 'no_password'
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        user ? user.enabled : true
    }
}
