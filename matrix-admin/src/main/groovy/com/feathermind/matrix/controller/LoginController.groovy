package com.feathermind.matrix.controller

import com.baomidou.kaptcha.Kaptcha
import com.baomidou.kaptcha.exception.KaptchaException
import com.baomidou.kaptcha.exception.KaptchaIncorrectException
import com.baomidou.kaptcha.exception.KaptchaNotFoundException
import com.baomidou.kaptcha.exception.KaptchaTimeoutException
import com.feathermind.matrix.controller.bean.CasConfig
import com.feathermind.matrix.controller.bean.LoginInfo
import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.security.TokenService
import com.feathermind.matrix.service.CasClientService
import com.feathermind.matrix.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import java.util.concurrent.ConcurrentHashMap

/**
 * 通过kaptchaFree方法检查当前用户名、客户端IP是否需要验证码
 * 如果需要，客户端要展示验证码，输入后传给后台
 */
@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = ["http://localhost:3000", "null"], allowCredentials = "true")
class LoginController {
    @Autowired
    CasClientService casClientService
    @Autowired
    UserService userService
    @Autowired
    TokenService userSecurityService
    @Autowired
    GormSessionBean gormSessionBean
    @Autowired
    private Kaptcha kaptcha;
    Map<String, Integer> loginErrorMap = new ConcurrentHashMap()

    @PostMapping("/login")
    ResponseEntity<LoginInfo> login(@RequestBody Map reqBody, HttpServletRequest request) {
        String ip = getClientIP(request)
        String username = reqBody.username;
        if (!isSafe(ip, username)) {
            def kResult = kaptchaValid(reqBody.kaptchaCode);
            if (!kResult.success) {
                return ResponseEntity.ok(new LoginInfo(kResult))
            }
        }
        def result = userService.login(username, reqBody.passwordHash);
        if (result.success) {
            def tokenDetails = userSecurityService.loadUserByUsername(username)
            if (gormSessionBean)
                gormSessionBean.tokenDetails = tokenDetails
            result << [
                    account    : username,
                    roles      : tokenDetails.roles,
                    authorities: tokenDetails.authorities,
                    token      : userSecurityService.generateToken(tokenDetails)]
            clearLoginError(ip, username)
        } else
            result.kaptchaFree = newLoginError(ip, username)
        ResponseEntity.ok(new LoginInfo(result))
    }

    /**
     * @param id 唯一id防止缓存用于刷新验证码
     */
    @GetMapping("/kaptcha/{id}")
    public void kaptchaRender(@PathVariable String id) {
        kaptcha.render();
    }


    Map kaptchaValid(String code) {
        def result = [success: false]
        try {
            kaptcha.validate(code, 300);
            result.success = true;
        }
        catch (KaptchaException kaptchaException) {
            if (kaptchaException instanceof KaptchaIncorrectException) {
                result.error = "验证码不正确";
            } else if (kaptchaException instanceof KaptchaNotFoundException) {
                result.error = "验证码未找到";
            } else if (kaptchaException instanceof KaptchaTimeoutException) {
                result.error = "验证码过期";
            }
        }
        return result
    }
    /**
     * 检查当前登录的用户名和客户端是否需要验证码
     * @param reqBody
     * @param request
     * @return 如果不需要，返回{success: true}
     */
    @PostMapping("/kaptchaFree")
    ResponseEntity<ResBean> kaptchaFree(@RequestBody Map reqBody, HttpServletRequest request) {
        String ip = getClientIP(request)
        ResponseEntity.ok(new ResBean(success: reqBody.username ? isSafe(ip, reqBody.username) : isSafe(ip)))
    }
    /**
     * 设置错误次数，key值为用户名或客户端IP
     * @param key
     */
    boolean newLoginError(String... keys) {
        keys.each { key ->
            def map = loginErrorMap
            def old = map.get(key);
            map.put(key, old ? old + 1 : 1)
        }
        return isSafe(keys)
    }

    /**
     * 登录成功后，清楚错误
     * @param keys
     */
    void clearLoginError(String... keys) {
        keys.each {
            loginErrorMap.remove(it)
        }
    }

    /**
     * 检查任何一个key值的错误次数是否超过一定数量
     * @param keys
     * @return true，代表需要验证码
     */
    boolean isSafe(String... keys) {
        return !keys.find { key ->
            def v = loginErrorMap.get(key)
            return v && v > 2
        }
    }

    /**
     * 支持CAS登录
     * @return
     */
    @PostMapping("/sessionLogin")
    ResponseEntity<LoginInfo> sessionLogin() {
        def tokenDetails = gormSessionBean.tokenDetails
        def result
        if (tokenDetails) {
            // 如果是CAS登录，user可能为null
            result = [success    : true,
                      user       : tokenDetails.user,
                      roles      : tokenDetails.roles,
                      authorities: tokenDetails.authorities,
                      account    : tokenDetails.username,
                      token      : userSecurityService.generateToken(tokenDetails)]
        } else
            result = [success: false,
                      error  : '服务端没有session信息']
        ResponseEntity.ok(new LoginInfo(result))
    }


    @PostMapping("/logout")
    ResponseEntity<ResBean> logout(HttpSession session) {
        gormSessionBean.tokenDetails = null;
        session.invalidate()
        ResponseEntity.ok(new ResBean([success: true]))
    }

    @PostMapping("/getCasConfig")
    ResponseEntity<CasConfig> getCasConfig() {
        return ResponseEntity.ok(new CasConfig([clientEnabled: casClientService.clientEnabled,
                                                casServerRoot: casClientService.configProps?.serverUrlPrefix,
                                                defaultRoles : casClientService.casDefaultRoles]))
    }

    String getClientIP(HttpServletRequest request) {
        def heads = ['X-Forwarded-For', 'X-Real-IP', 'Proxy-Client-IP', 'WL-Proxy-Client-IP']
        for (String h : heads) {
            def v = request.getHeader(h);
            if (v && v.length() >= 7)
                return v
        }
        return request.remoteAddr
    }
}
