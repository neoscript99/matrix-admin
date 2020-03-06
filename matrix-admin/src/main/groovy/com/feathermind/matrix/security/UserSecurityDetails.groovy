package com.feathermind.matrix.security

import com.feathermind.matrix.domain.sys.User
import groovy.transform.TupleConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@TupleConstructor(includes = 'user,roleList')
class UserSecurityDetails implements UserDetails {
    User user
    List<GrantedAuthority> roleList

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList
    }

    @Override
    String getPassword() {
        return user.password
    }

    @Override
    String getUsername() {
        return user.name
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
        return user.enabled
    }
}
