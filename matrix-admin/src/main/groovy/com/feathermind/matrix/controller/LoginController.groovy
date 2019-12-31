package com.feathermind.matrix.controller

import com.feathermind.matrix.controller.bean.CasConfig
import com.feathermind.matrix.controller.bean.LoginInfo
import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.service.CasClientService
import com.feathermind.matrix.service.TokenService
import com.feathermind.matrix.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = ["http://localhost:3000", "null"], allowCredentials = "true")
class LoginController {
    @Autowired
    CasClientService casClientService
    @Autowired
    TokenService tokenService
    @Autowired
    UserService userService
    @Autowired
    GormSessionBean gormSessionBean

    @PostMapping("/login")
    ResponseEntity<LoginInfo> login(@RequestBody Map req) {
        def result = userService.login(req.username, req.passwordHash);

        if (result.success) {
            def roles = userService.getUserRoleCodes(result.user)
            def token = tokenService.createToken(result.user.account, roles)
            if (gormSessionBean)
                gormSessionBean.token = token
            result << [
                    account: result.user.account,
                    roles  : roles,
                    token  : token.id]
        }
        ResponseEntity.ok(new LoginInfo(result))
    }

    @PostMapping("/sessionLogin")
    ResponseEntity<LoginInfo> sessionLogin() {
        def token = gormSessionBean.token
        def result
        if (token) {
            def user = userService.findByAccount(token.username)
            result = [success: true,
                      user   : user,
                      roles  : token.roles,
                      account: token.username,
                      token  : token.id]
        } else
            result = [success: false,
                      error  : '服务端没有session信息']
        ResponseEntity.ok(new LoginInfo(result))
    }


    @PostMapping("/logout")
    ResponseEntity<ResBean> logout(HttpSession session) {
        session.invalidate()
        if (gormSessionBean.token) {
            tokenService.destoryToken(gormSessionBean.token.id)
            gormSessionBean.token = null
        }
        ResponseEntity.ok(new ResBean([success: true]))
    }

    @PostMapping("/getCasConfig")
    ResponseEntity<CasConfig> getCasConfig() {
        return ResponseEntity.ok(new CasConfig([clientEnabled: casClientService.clientEnabled,
                                                casServerRoot: casClientService.configProps?.serverUrlPrefix,
                                                defaultRoles : casClientService.casDefaultRoles]))
    }
}
