package com.feathermind.matrix.service

import net.unicon.cas.client.configuration.CasClientConfigurationProperties
import org.jasig.cas.client.util.AssertionHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CasClientService {
    @Value('${matrix.cas.client.enabled}')
    Boolean clientEnabled
    @Autowired(required = false)
    CasClientConfigurationProperties configProps;

    String getCasAccount() {
        if (AssertionHolder.assertion)
            return AssertionHolder.assertion.principal.name
    }
    String getLogoutUrl() {
        if (configProps)
            return "$configProps.serverUrlPrefix/logout?service=$configProps.clientHostUrl/index.html"
    }
}
