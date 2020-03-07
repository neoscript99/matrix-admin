package com.feathermind.matrix.security

import com.feathermind.matrix.controller.GormSessionBean
import com.feathermind.matrix.service.UserService
import com.feathermind.matrix.util.JwtUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    GormSessionBean gormSessionBean
    @Autowired
    UserSecurityService userSecurityService;
    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        UserDetails userDetails = /*gormSessionBean.tokenDetails ?:*/ parseToken(request);
        if (userDetails) {
            def authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
        // todo 清理还有问题，需继续处理
        //  SecurityContextHolder.getContext().setAuthentication('anonymousUser');
    }

    private UserDetails parseToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }
        String token = header.split(" ")[1];
        String username = JwtUtil.getUsername(token);
        try {
            def user = userService.findByAccount(username)
            if (user && JwtUtil.verify(token, user.password)) return userSecurityService.loadUserByUsername(username);
            else log.warn('用户{}的token过期', username)
        } catch (UsernameNotFoundException e) {
            log.error(e.message, e);
            return null;
        }
    }
}
