package com.feathermind.matrix.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext

@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
class GormSessionBean {
    @Autowired
    com.feathermind.matrix.service.CasClientService casClientService

    private com.feathermind.matrix.domain.sys.Token token

    com.feathermind.matrix.domain.sys.Token getToken() {
        if (!token && casClientService.clientEnabled)
            token = casClientService.createTokenByCas()
        return token
    }

    void setToken(com.feathermind.matrix.domain.sys.Token token) {
        this.token = token
    }
}
