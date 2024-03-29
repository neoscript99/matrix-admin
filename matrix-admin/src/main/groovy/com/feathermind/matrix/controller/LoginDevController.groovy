package com.feathermind.matrix.controller

import com.feathermind.matrix.controller.bean.LoginInfo
import com.feathermind.matrix.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.*


/**
 * 通过kaptchaFree方法检查当前用户名、客户端IP是否需要验证码
 * 如果需要，客户端要展示验证码，输入后传给后台
 */
@RestController
@RequestMapping("/api/login")
@ConditionalOnExpression('"dev".equals("${spring.profiles.active}") or ${matrix.dev-login:false}')
class LoginDevController {
    @Autowired
    UserService userService
    @Autowired
    LoginController loginController

    @PostMapping("/devLogin")
    LoginInfo devLogin(@RequestBody Map reqBody) {
        String username = reqBody.username;
        def user = username ? userService.findByAccount(username) : null;
        user ? loginController.afterLogin(user) : new LoginInfo(success: false, error: "$username 用户不存在")
    }
}
