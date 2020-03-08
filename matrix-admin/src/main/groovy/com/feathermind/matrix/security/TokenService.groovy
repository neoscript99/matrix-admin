package com.feathermind.matrix.security

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.RoleService
import com.feathermind.matrix.service.UserService
import com.feathermind.matrix.util.JwtUtil
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@CompileStatic
@Slf4j
class TokenService {

    @Value('${gorm.token.expire.minutes}')
    long expireMinutes
    @Autowired
    UserService userService
    @Autowired
    RoleService roleService

    TokenDetails loadUserByUsername(String username) {
        User user = userService.findByAccount(username)
        if (!user) {
            log.error("用户不存在：$username")
            return null;
        } else {
            def roles = userService.getUserRoles(user)
            return new TokenDetails(user, roles)
        }
    }

    TokenDetails loadUserWithDefaultRoles(String username, String defaultRoles) {
        def td = loadUserByUsername(username)
        return td ?: new TokenDetails(username,
                roleService.findByCodes(defaultRoles.split(',')))
    }

    String generateToken(TokenDetails tokenDetails) {
        JwtUtil.generate(tokenDetails.username, tokenDetails.password, expireMinutes)
    }
}
