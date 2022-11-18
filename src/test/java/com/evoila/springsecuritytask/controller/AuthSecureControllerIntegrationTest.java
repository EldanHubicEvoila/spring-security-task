package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.exception.InvalidRoleException;
import com.evoila.springsecuritytask.exception.UserAlreadyExistsException;
import com.evoila.springsecuritytask.payload.request.AuthenticationRequest;
import com.evoila.springsecuritytask.payload.request.RegistrationRequest;
import com.evoila.springsecuritytask.payload.response.AuthenticationResponse;
import com.evoila.springsecuritytask.payload.response.RegistrationResponse;
import com.evoila.springsecuritytask.service.AuthenticationService;
import com.evoila.springsecuritytask.service.RegistrationService;
import com.evoila.springsecuritytask.service.UserService;
import com.evoila.springsecuritytask.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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


@WebMvcTest(AuthController.class)
class AuthSecureControllerIntegrationTest extends AbstractSecureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    RegistrationService registrationService;


    @Nested
    class Login {
        @Test
        @DisplayName("login()_validCredentials_200")
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
        @DisplayName("login()_invalidCredentials_401")
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
        @DisplayName("login()_usernameNull_400")
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
        @DisplayName("login()_passwordNull_400")
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

    @Nested
    class Register {
        @Test
        @DisplayName("register()_validRegistrationRequest_200")
        void register_whenValidRegistrationRequest_shouldRegisterUserAndReturnRegistrationResponse() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setEmail("testemail@email.com");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setRole("user");

            Mockito.when(registrationService.register(any(RegistrationRequest.class)))
                    .thenReturn(RegistrationResponse.builder()
                            .message("Test success message")
                            .username("testUser")
                            .email("testemail@email.com")
                            .build());

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Test success message"))
                    .andExpect(jsonPath("$.username").value(testRegistrationRequest.getUsername()))
                    .andExpect(jsonPath("$.email").value(testRegistrationRequest.getEmail()));
        }

        @Test
        @DisplayName("register()_userWithUsernameAlreadyExists_409")
        void register_whenUserWithUsernameAlreadyExists_shouldThrowUserAlreadyExistsException() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setEmail("testemail@email.com");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setRole("user");

            Mockito.when(registrationService.register(any(RegistrationRequest.class)))
                    .thenThrow(UserAlreadyExistsException.class);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("register()_userWithEmailAlreadyExists_409")
        void register_whenUserWithEmailAlreadyExists_shouldThrowUserAlreadyExistsException() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setEmail("testemail@email.com");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setRole("user");

            Mockito.when(registrationService.register(any(RegistrationRequest.class)))
                    .thenThrow(UserAlreadyExistsException.class);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("register()_usernameNull_400")
        void register_whenUsernameNull_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername(null);
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail("testemail@email.com");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("register()_usernameEmpty_400")
        void register_whenUsernameEmpty_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail("testemail@email.com");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("register()_passwordNull_400")
        void register_whenPasswordNull_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword(null);
            testRegistrationRequest.setEmail("testemail@email.com");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("register()_passwordEmpty_400")
        void register_whenPasswordEmpty_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword("");
            testRegistrationRequest.setEmail("testemail@email.com");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("register()_emailNull_400")
        void register_whenEmailNull_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail(null);

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("register()_emailEmpty_400")
        void register_whenEmailEmpty_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail("");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("register()_badEmailFormat_400")
        void register_whenBadEmailFormat_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail("bademailformat");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("register()_invalidRole_400")
        void register_whenInvalidRole_shouldThrowInvalidRoleException_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail("testemail@email.com");
            testRegistrationRequest.setRole("invalid-role");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenThrow(InvalidRoleException.class);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("register()_roleNull_400")
        void register_whenRoleNull_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail("testemail@email.com");
            testRegistrationRequest.setRole(null);

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("register()_emptyRole_400")
        void register_whenRoleEmpty_shouldReturnBadRequest_400() throws Exception{
            RegistrationRequest testRegistrationRequest = new RegistrationRequest();
            testRegistrationRequest.setUsername("testUser");
            testRegistrationRequest.setPassword("testPw");
            testRegistrationRequest.setEmail("testemail@email.com");
            testRegistrationRequest.setRole("");

            Mockito.when(registrationService.register(any(RegistrationRequest.class))).thenReturn(null);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.toJson(testRegistrationRequest)))
                    .andExpect(status().isBadRequest());
        }
    }
}