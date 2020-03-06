package com.feathermind.matrix.security

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
    UserSecurityService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        getAuthentication(request);
        chain.doFilter(request, response);
    }

    private void getAuthentication(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }
        String token = header.split(" ")[1];
        String username = JwtUtil.getUsername(token);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (JwtUtil.verify(token, userDetails.getPassword())) {
                def authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e) {
            log.error(e.message, e);
        }
    }
}
