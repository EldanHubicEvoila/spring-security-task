package com.evoila.springsecuritytask.security;


import com.evoila.springsecuritytask.model.AuthenticationRequest;
import com.evoila.springsecuritytask.model.AuthenticationUser;
import com.evoila.springsecuritytask.model.User;
import com.evoila.springsecuritytask.repository.UserRepository;
import com.evoila.springsecuritytask.service.AuthenticationService;
import com.evoila.springsecuritytask.util.JsonUtil;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class JwtIntegrationTest extends SecurityConfigTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.2-alpine"));;


    @DynamicPropertySource
    static void overrideTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
    }
    private final AuthenticationUser authenticationUser = new AuthenticationUser(
            new User("testUser", "testemail@email.com", "testPw"));


    @Test
    void getEmployees_whenValidJWT_shouldReturnAllEmployees_200() throws Exception {
        String password = bCryptPasswordEncoder.encode("testPw");
        AuthenticationUser authenticationUser = new AuthenticationUser(
                new User("testUser", "testemail@email.com", password));
        userRepository.save(authenticationUser.getUser());
        Optional<User> user = userRepository.findByUsername("testUser");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testUser");
        authenticationRequest.setPassword("testPw");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(authenticationRequest)))
                .andExpect(status().isOk()).andDo(print());
    }

}
