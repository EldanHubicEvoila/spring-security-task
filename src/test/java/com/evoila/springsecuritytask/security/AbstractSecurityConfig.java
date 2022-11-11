package com.evoila.springsecuritytask.security;

import com.evoila.springsecuritytask.util.JwtTokenUtil;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;


@ImportAutoConfiguration({
        SecurityConfig.class,
        JwtTokenUtil.class
})
public abstract class AbstractSecurityConfig {

    @MockBean
    private UserDetailsService userDetailsService;
}