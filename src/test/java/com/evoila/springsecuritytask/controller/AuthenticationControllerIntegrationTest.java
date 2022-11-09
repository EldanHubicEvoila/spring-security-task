package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.config.SecurityConfigTest;
import com.evoila.springsecuritytask.model.AuthenticationRequest;
import com.evoila.springsecuritytask.model.AuthenticationResponse;
import com.evoila.springsecuritytask.service.AuthenticationService;
import com.evoila.springsecuritytask.util.JsonUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerIntegrationTest extends SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;


    @Test
    @DisplayName("login(AuthenticationRequest authenticationRequest)_validCredentials_200")
    void login_whenValidUsernameAndPassword_returnAuthenticationResponse_200() throws Exception {
        AuthenticationRequest testAuthenticationRequest = new AuthenticationRequest();
        testAuthenticationRequest.setUsername("testUser");
        testAuthenticationRequest.setPassword("testPw");
        AuthenticationResponse testAuthenticationResponse = AuthenticationResponse.builder()
                .username("testUser")
                .email("testemail@email.com")
                .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                        ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6InRlc3RVc2VyIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
                        ".UxNJRmZD70Jv4BX2UYmJE8vQhWVn8OSJTA5McJSPce4")
                .build();

        Mockito.when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(testAuthenticationResponse);


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testAuthenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testAuthenticationResponse.getUsername()))
                .andExpect(jsonPath("$.email").value(testAuthenticationResponse.getEmail()))
                .andExpect(jsonPath("$.accessToken").value(testAuthenticationResponse.getAccessToken()));
    }

    @Test
    @DisplayName("login(AuthenticationRequest authenticationRequest)_invalidCredentials_401")
    void login_whenInvalidUsernameAndPassword_throwBadCredentialsException_401() throws Exception{
        AuthenticationRequest testAuthenticationRequest = new AuthenticationRequest();
        testAuthenticationRequest.setUsername("testUser");
        testAuthenticationRequest.setPassword("testPw");

        Mockito.when(authenticationService.login(any(AuthenticationRequest.class))).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testAuthenticationRequest)))
                        .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("login(AuthenticationRequest authenticationRequest)_usernameNull_400")
    void login_whenUsernameNull_shouldReturnBadRequest_400() throws Exception{
        AuthenticationRequest testAuthenticationRequest = new AuthenticationRequest();
        testAuthenticationRequest.setUsername(null);
        testAuthenticationRequest.setPassword("testPw");

        Mockito.when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testAuthenticationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("login(AuthenticationRequest authenticationRequest)_passwordNull_400")
    void login_whenPasswordNull_shouldReturnBadRequest_400() throws Exception{
        AuthenticationRequest testAuthenticationRequest = new AuthenticationRequest();
        testAuthenticationRequest.setUsername("testUser");
        testAuthenticationRequest.setPassword(null);

        Mockito.when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testAuthenticationRequest)))
                .andExpect(status().isBadRequest());
    }
}