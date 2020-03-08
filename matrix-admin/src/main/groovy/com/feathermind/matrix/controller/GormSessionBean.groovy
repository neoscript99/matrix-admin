package com.feathermind.matrix.controller

import com.feathermind.matrix.security.TokenDetails
import com.feathermind.matrix.security.TokenService
import com.feathermind.matrix.service.CasClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext

/**
 * 仅用于用户信息缓存，如果session关闭，不影响功能
 * 用户信息将通过JwtAuthorizationFilter解析header中的token获取
 */
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
class GormSessionBean {
    @Autowired
    CasClientService casClientService
    @Autowired
    TokenService userSecurityService

    TokenDetails tokenDetails

    TokenDetails getTokenDetails() {
        if (!tokenDetails && casClientService.clientEnabled && casClientService.casAccount) {
            tokenDetails = userSecurityService.loadUserWithDefaultRoles(casClientService.casAccount, casClientService.casDefaultRoles)
        }
        return tokenDetails
    }
}
