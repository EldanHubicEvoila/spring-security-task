package com.evoila.springsecuritytask.security;


import com.evoila.springsecuritytask.container.AbstractAuthentificationJwtConfig;
import com.evoila.springsecuritytask.model.AuthenticationRequest;
import com.evoila.springsecuritytask.model.Employee;
import com.evoila.springsecuritytask.repository.EmployeeRepository;
import com.evoila.springsecuritytask.util.JsonUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



public class EmployeeJwtIntegrationTest extends AbstractAuthentificationJwtConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;


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

    @Test
    @DisplayName("createEmployee(Employee employee)_validJwt_201")
    void createEmployee_whenJwtValid_shouldReturnCreatedEmployee_201() throws Exception {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("testFirstName");
        testEmployee.setLastName("testLastName");
        testEmployee.setEmail("testEmail@email.com");

        AuthenticationRequest authenticationRequest = createTestAuthenticationRequest();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("getEmployeeById(Long id)_validJWT_200")
    void getEmployeeById_whenJwtValid_shouldReturnEmployee_200() throws Exception {
        AuthenticationRequest authenticationRequest = createTestAuthenticationRequest();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(get("/api/v1/employees/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("updateEmployee(Employee employeeDetails, Long id)_validJWT_200")
    void updateEmployee_whenJwtValid_shouldReturnUpdatedEmployee_200() throws Exception {
        Employee employeeDetails = new Employee();
        employeeDetails.setFirstName("testFirstName");
        employeeDetails.setLastName("testLastName");
        employeeDetails.setEmail("testEmail@email.com");
        employeeRepository.save(createTestEmployee());

        AuthenticationRequest authenticationRequest = createTestAuthenticationRequest();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(put("/api/v1/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(JsonUtil.toJson(employeeDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("deleteEmployee(Long id)_validJWT_200")
    void deleteEmployee_whenJwtValid_shouldDeleteEmployeeAndReturnTrue_200() throws Exception {
        employeeRepository.save(createTestEmployee());

        AuthenticationRequest authenticationRequest = createTestAuthenticationRequest();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody);

        mockMvc.perform(delete("/api/v1/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getEmployees_jwtNull_401")
    void getEmployees_whenJwtNull_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + null))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("getEmployees()_jwtInvalid_401")
    void getEmployees_whenJwtInvalid_shouldReturnUnauthorized_401() throws Exception {
        String token = "invalid-token";

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("getEmployees()_jwtBadSignature_401")
    void getEmployees_whenJwtBadSignature_shouldReturnUnauthorized_401() throws Exception {
        AuthenticationRequest authenticationRequest = createTestAuthenticationRequest();
        String responseBody = getResponseBodyAfterLogin(authenticationRequest);
        String token = extractJwtFromResponseBody(responseBody) + "a";

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    private Employee createTestEmployee() {
        Employee testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("testFirstName");
        testEmployee.setLastName("testLastName");
        testEmployee.setEmail("testEmail@email.com");

        return testEmployee;
    }
}
