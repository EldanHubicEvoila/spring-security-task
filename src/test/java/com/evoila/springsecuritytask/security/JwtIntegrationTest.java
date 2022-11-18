package com.evoila.springsecuritytask.security;


import com.evoila.springsecuritytask.dto.EmployeeDTO;
import com.evoila.springsecuritytask.model.*;
import com.evoila.springsecuritytask.payload.request.AuthenticationRequest;
import com.evoila.springsecuritytask.repository.UserRepository;
import com.evoila.springsecuritytask.util.JsonUtil;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

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

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JwtIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.2-alpine"));


    @DynamicPropertySource
    static void overrideTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
    }

    @BeforeEach
    private void initUser() {
        userRepository.save(createUser());
        userRepository.save(createAdmin());
    }

    @AfterEach
    private void clearUserTable() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("getEmployees()_validJWT_200")
    void getEmployees_whenJwtValid_shouldReturnAllEmployees_200() throws Exception {
        AuthenticationRequest authenticationRequest = createTestAuthenticationRequestForUser();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("createEmployee()_validJWT_admin_200")
    void createEmployee_whenJwtValidAndAdmin_shouldCreateAndReturnEmployee_200() throws Exception {
        EmployeeDTO testEmployeeDTO = EmployeeDTO.builder()
                .id(1L)
                .firstName("testFirstName")
                .lastName("testLastName")
                .email("testEmployeeEmail@email.com")
                .build();

        AuthenticationRequest authenticationRequest = createTestAuthenticationRequestForAdmin();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("createEmployee()_validJWT_user_403")
    void createEmployee_whenJwtValidAndUser_shouldReturnForbidden_403() throws Exception {
        EmployeeDTO testEmployeeDTO = EmployeeDTO.builder()
                .id(1L)
                .firstName("testFirstName")
                .lastName("testLastName")
                .email("testEmployeeEmail@email.com")
                .build();

        AuthenticationRequest authenticationRequest = createTestAuthenticationRequestForUser();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    private User createUser() {
        String password = bCryptPasswordEncoder.encode("testPw");
        User testUser = new User("testUser", "testemail@email.com", password);
        Set<Role> roles = new HashSet<>();
        Role testRole = new Role();
        testRole.setName("USER");
        roles.add(testRole);
        testUser.setRoles(roles);

        return testUser;
    }

    private User createAdmin() {
        String password = bCryptPasswordEncoder.encode("testPw");
        User testAdmin = new User("testAdmin", "testAdminEmail@email.com", password);
        Set<Role> roles = new HashSet<>();
        Role testRole = new Role();
        testRole.setName("ADMIN");
        roles.add(testRole);
        testAdmin.setRoles(roles);

        return testAdmin;
    }

    public AuthenticationRequest createTestAuthenticationRequestForUser() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testUser");
        authenticationRequest.setPassword("testPw");

        return authenticationRequest;
    }

    public AuthenticationRequest createTestAuthenticationRequestForAdmin() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testAdmin");
        authenticationRequest.setPassword("testPw");

        return authenticationRequest;
    }

    public String getResponseBodyAfterLogin(AuthenticationRequest authenticationRequest) throws Exception {
        return mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(authenticationRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    public String extractJwtFromResponseBody(String responseBody) throws Exception {
        JSONObject jsonObject = new JSONObject(responseBody);

        return jsonObject.getString("accessToken");
    }
}
