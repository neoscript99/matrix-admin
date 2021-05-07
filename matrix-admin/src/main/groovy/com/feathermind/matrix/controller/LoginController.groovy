package com.feathermind.matrix.controller

import cn.hutool.core.bean.BeanUtil
import com.baomidou.kaptcha.Kaptcha
import com.baomidou.kaptcha.exception.KaptchaException
import com.baomidou.kaptcha.exception.KaptchaIncorrectException
import com.baomidou.kaptcha.exception.KaptchaNotFoundException
import com.baomidou.kaptcha.exception.KaptchaTimeoutException
import com.feathermind.matrix.util.MatrixException
import com.feathermind.matrix.wechat.bean.WxUserInfo
import com.feathermind.matrix.config.MatrixConfigProperties
import com.feathermind.matrix.controller.bean.LoginInfo
import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.domain.sys.UserBind
import com.feathermind.matrix.security.TokenService
import com.feathermind.matrix.service.CasClientService
import com.feathermind.matrix.service.UserBindService
import com.feathermind.matrix.service.UserService
import com.feathermind.matrix.wechat.WechatBinder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
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
class LoginController implements WechatBinder {
    @Autowired
    MatrixConfigProperties matrixConfigProperties
    @Autowired
    CasClientService casClientService
    @Autowired
    UserService userService
    @Autowired
    UserBindService userBindService
    @Autowired
    TokenService userSecurityService
    @Autowired
    GormSessionBean gormSessionBean
    @Autowired
    private Kaptcha kaptcha;
    @Autowired
    Environment env;

    Map<String, Integer> loginErrorMap = new ConcurrentHashMap()

    @PostMapping("/login")
    LoginInfo login(@RequestBody Map reqBody, HttpServletRequest request) {
        String ip = getClientIP(request)
        String username = reqBody.username;
        if (!isSafe(ip, username)) {
            def kResult = kaptchaValid(reqBody.kaptchaCode);
            if (!kResult.success) {
                return new LoginInfo(kResult)
            }
        }
        def result = userService.login(username, reqBody.passwordHash);
        if (result.success) {
            result << afterLogin(result.user)
            clearLoginError(ip, username)
        } else
            result.kaptchaFree = newLoginError(ip, username)
        new LoginInfo(result)
    }

    Map afterLogin(User user) {
        def tokenDetails = userSecurityService.loadUserByUsername(user.account)
        if (gormSessionBean)
            gormSessionBean.tokenDetails = tokenDetails
        return [
                success    : true,
                user       : user,
                account    : user.account,
                roles      : tokenDetails.roles,
                authorities: tokenDetails.authorities,
                token      : userSecurityService.generateToken(tokenDetails)]
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
    ResBean kaptchaFree(@RequestBody Map reqBody, HttpServletRequest request) {
        String ip = getClientIP(request)
        new ResBean(success: reqBody.username ? isSafe(ip, reqBody.username) : isSafe(ip))
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
    LoginInfo sessionLogin() {
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
        new LoginInfo(result)
    }


    @PostMapping("/logout")
    ResBean logout(HttpSession session) {
        gormSessionBean.tokenDetails = null;
        session.invalidate()
        new ResBean([success: true])
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

    @Override
    Map bindWechat(WxUserInfo wxUserInfo) {
        def bind = BeanUtil.toBean(wxUserInfo, UserBind);
        bind.source = 'wechat'
        User user = userBindService.getOrCreateUser(bind)
        if (!user.enabled)
            throw new MatrixException('UserDisabled', '用户帐号失效')
        return afterLogin(user)
    }
}
