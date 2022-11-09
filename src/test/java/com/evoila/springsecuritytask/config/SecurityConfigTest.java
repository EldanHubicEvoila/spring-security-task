package com.evoila.springsecuritytask.config;

import com.evoila.springsecuritytask.security.SecurityConfig;
import com.evoila.springsecuritytask.util.JwtTokenUtil;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser
@ImportAutoConfiguration({
        SecurityConfig.class,
        JwtTokenUtil.class
})
public abstract class SecurityConfigTest {
}
