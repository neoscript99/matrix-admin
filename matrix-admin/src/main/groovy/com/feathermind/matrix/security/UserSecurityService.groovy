package com.feathermind.matrix.security

import com.feathermind.matrix.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserSecurityService implements UserDetailsService {
    private Map<String, UserDetails> userCache = [:]
    @Autowired
    UserService userService

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        def detail = userCache.get(username);
        if (detail) return detail;

        def user = userService.findByAccount(username)
        if (!user) throw new UsernameNotFoundException("用户不存在：$username")

        def roles = userService.getUserRoles(user)
        def newDetail = new UserSecurityDetails(user, roles.collect { new SimpleGrantedAuthority(it.roleCode) })
        userCache.put(username, newDetail)
        return newDetail
    }

    void removeDetails(String username) {
        userCache.remove(username)
    }
}
