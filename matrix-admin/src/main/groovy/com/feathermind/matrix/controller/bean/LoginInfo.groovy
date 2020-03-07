package com.feathermind.matrix.controller.bean

import com.feathermind.matrix.domain.sys.User

class LoginInfo extends ResBean {
    String token
    User user
    Collection<String> roles
    Collection<String> authorities
    String account
    Boolean kaptchaFree
}
