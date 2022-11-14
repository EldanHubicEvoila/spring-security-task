package com.evoila.springsecuritytask.security;


import com.evoila.springsecuritytask.container.AbstractAuthentificationJwtConfig;
import com.evoila.springsecuritytask.model.AuthenticationRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class JwtIntegrationTest extends AbstractAuthentificationJwtConfig {

    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName("getEmployees()_validJWT_200")
    void getEmployees_whenJwtValid_shouldReturnAllEmployees_200() throws Exception {
        AuthenticationRequest authenticationRequest = createTestAuthenticationRequest();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
