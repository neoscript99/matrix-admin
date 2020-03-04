package com.feathermind.matrix.controller.bean

import com.feathermind.matrix.domain.sys.User

class LoginInfo extends ResBean{
    String token
    User user
    String roles
    String account
    Boolean kaptchaFree
}
