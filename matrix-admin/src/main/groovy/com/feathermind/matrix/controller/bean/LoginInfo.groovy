package com.feathermind.matrix.controller.bean

import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.service.bean.LoginResult

class LoginInfo extends ResBean {
    LoginInfo() {
    }

    LoginInfo(LoginResult result) {
        this.user = result.user
        this.success = result.success
        this.error = result.error
    }
    String token
    User user
    Collection<String> roles
    Collection<String> authorities
    String account
    Boolean kaptchaFree
    Boolean isInitPassword
}
