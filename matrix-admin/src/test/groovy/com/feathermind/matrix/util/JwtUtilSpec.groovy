package com.feathermind.matrix.util

import spock.lang.Specification

class JwtUtilSpec extends Specification {
    def 'jwt build'() {
        given:
        def token = JwtUtil.generate('admin', 'pass')
        println token;
        println JwtUtil.getUsername(token);

        expect:
        JwtUtil.verify(token, 'pass')
        !JwtUtil.verify(token, 'pass2')
        !JwtUtil.verify("error token", 'pass')
    }
}
