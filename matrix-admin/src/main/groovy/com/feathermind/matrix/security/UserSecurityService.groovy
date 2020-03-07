package com.feathermind.matrix.security

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.RoleService
import com.feathermind.matrix.service.UserService
import com.feathermind.matrix.util.JwtUtil
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
@CompileStatic
class UserSecurityService implements UserDetailsService {

    @Value('${gorm.token.expire.minutes}')
    long expireMinutes
    @Autowired
    UserService userService
    @Autowired
    RoleService roleService

    @Override
    TokenDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByAccount(username)
        if (!user) throw new UsernameNotFoundException("用户不存在：$username")

        def roles = userService.getUserRoles(user)
        return new TokenDetails(user, roles)
    }

    TokenDetails loadUserWithDefaultRoles(String username, String defaultRoles) {
        try {
            return loadUserByUsername(username)
        } catch (UsernameNotFoundException e) {
            return new TokenDetails(username,
                    roleService.findByCodes(defaultRoles.split(',')))
        }
    }

    String generateToken(UserDetails userDetails) {
        JwtUtil.generate(userDetails.username, userDetails.password, expireMinutes)
    }
}
