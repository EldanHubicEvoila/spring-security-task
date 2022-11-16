package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.security.SecurityConfig;
import com.evoila.springsecuritytask.util.JwtTokenUtil;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;


@ImportAutoConfiguration({
        SecurityConfig.class,
        JwtTokenUtil.class
})
public abstract class AbstractSecureControllerTest {

    @MockBean
    private UserDetailsService userDetailsService;
}
