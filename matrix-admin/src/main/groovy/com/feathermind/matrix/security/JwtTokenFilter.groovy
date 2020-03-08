package com.feathermind.matrix.security

import com.feathermind.matrix.controller.GormSessionBean
import com.feathermind.matrix.util.JwtUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * token过期时间为固定值，无法根据最近访问延时
 * session过期时间是最后一次访问之后计时，容器自动缓存，这种方式更合理，单体系统优先使用session
 *
 * todo 实现分布式待优化内容：
 * 1.全部改为http header的Authorization方式
 * 2.tokenService增加缓存，不用每次查库
 * 3.开发jwt刷新接口
 * 4.客户端开发jwt到期检查并调用刷新接口进行刷新
 * 5.开发openid功能，支持第三方调用接口时的权限控制
 *
 */
@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    GormSessionBean gormSessionBean
    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        TokenDetails tokenDetails = gormSessionBean.tokenDetails ?: parseToken(request);
        TokenHolder.setToken(tokenDetails)

        chain.doFilter(request, response);
        // 由于使用线程池，每次执行后必须做清理，否则会被其它用户获取
        TokenHolder.clear();
    }

    /**
     * CAS的话，用户可能没有在系统用户表中，有时不能使用本方法获取信息，只能通过上面的session做处理
     * 本方法不抛出异常，如果token有问题，通过后端controller控制权限
     * @param request
     * @return 成功解析token并校验有效期，返回对应TokenDetails，否则返回null
     */
    private TokenDetails parseToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        String token = header.split(" ")[1];

        String username = JwtUtil.getUsername(token);
        if (!username) return null;

        def tokenDetails = tokenService.loadUserByUsername(username);
        if (JwtUtil.verify(token, tokenDetails.password)) return tokenDetails
        else {
            log.error('用户{}的token验证失败', username)
            return null
        }
    }
}
