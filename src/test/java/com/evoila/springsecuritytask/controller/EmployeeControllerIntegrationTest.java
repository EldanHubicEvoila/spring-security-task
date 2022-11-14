package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.security.AbstractSecurityConfig;
import com.evoila.springsecuritytask.exception.ResourceNotFoundException;
import com.evoila.springsecuritytask.model.Employee;
import com.evoila.springsecuritytask.service.EmployeeService;
import com.evoila.springsecuritytask.util.JsonUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
class EmployeeControllerIntegrationTest extends AbstractSecurityConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    Employee testEmployee =
            new Employee(1L, "testFirstName", "testLastName", "testemail@email.com");


    @Test
    @WithMockUser
    @DisplayName("getEmployees()_200")
    void getEmployees_shouldReturnAllEmployees_200() throws Exception {
        List<Employee> testEmployees = new ArrayList<>(List.of(
                testEmployee));

        when(employeeService.getEmployees())
                .thenReturn(testEmployees);

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(testEmployee.getLastName()))
                .andExpect(jsonPath("$[0].email").value(testEmployee.getEmail()));
    }

    @Test
    @DisplayName("getEmployees()_unauthenticatedUser_401")
    void getEmployees_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("createEmployee(Employee employee)_201")
    void createEmployee_shouldCreateNewEmployee_201() throws Exception {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("testFirstName");
        testEmployee.setLastName("testLastName");
        testEmployee.setEmail("testEmail@email.com");

        when(employeeService.createEmployee(any(Employee.class)))
                .thenReturn(testEmployee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployee.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));
    }

    @Test
    @WithMockUser
    @DisplayName("createEmployee(Employee employee)_fieldNull_400")
    void createEmployee_whenFieldNull_shouldReturnBadRequest_400() throws Exception {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName(null);
        testEmployee.setLastName("testLastName");
        testEmployee.setEmail("testEmail@email.com");

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("createEmployee(Employee employee)_badEmailFormat_400")
    void createEmployee_whenBadEmailFormat_shouldReturnBadRequest_400() throws Exception {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("testFirstName");
        testEmployee.setLastName("testLastName");
        testEmployee.setEmail("badEmailFormat");

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("createEmployee(Employee employee)_unauthenticatedUser_401")
    void createEmployee_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("getEmployeeById(Long id)_200")
    void getEmployeeById_whenEmployeeExists_shouldGetEmployeeById_200() throws Exception {
        when(employeeService.getEmployeeById(testEmployee.getId()))
                .thenReturn(testEmployee);

        mockMvc.perform(get("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployee.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));
    }

    @Test
    @WithMockUser
    @DisplayName("getEmployeeById(Long id)_noEmployeeFound_404")
    void getEmployeeById_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.getEmployeeById(testEmployee.getId()))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getEmployeeById(Long id)_unauthenticatedUser_401")
    void getEmployeeById_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("updateEmployee(Long id, Employee employeeDetails)_200")
    void updateEmployee_shouldUpdateExistingEmployee_200() throws Exception {
        when(employeeService.updateEmployee(eq(testEmployee.getId()), any(Employee.class)))
                .thenReturn(testEmployee);

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployee.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));
    }

    @Test
    @WithMockUser
    @DisplayName("updateEmployee(Long id, Employee employeeDetails)_fieldNull_400")
    void updateEmployee_whenFieldNull_shouldReturnBadRequest_400() throws Exception {
        testEmployee.setEmail(null);

        when(employeeService.updateEmployee(testEmployee.getId(), testEmployee))
                .thenReturn(testEmployee);

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("updateEmployee(Long id, Employee employeeDetails)_badEmailFormat_400")
    void updateEmployee_whenBadEmailFormat_shouldReturnBadRequest_400() throws Exception {
        testEmployee.setEmail("badEmailFormat");

        when(employeeService.updateEmployee(testEmployee.getId(), testEmployee))
                .thenReturn(testEmployee);

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("updateEmployee(Long id, Employee employeeDetails)_noEmployeeFound_404")
    void updateEmployee_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.updateEmployee(eq(testEmployee.getId()), any(Employee.class)))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateEmployee(Long id, Employee employeeDetails)_unauthenticatedUser_401")
    void updateEmployee_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteEmployee(Long id)_200")
    void deleteEmployee_shouldDeleteExistingEmployee_200() throws Exception {
        when(employeeService.deleteEmployee(testEmployee.getId()))
                .thenReturn(true);

        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteEmployee(Long id)_noEmployeeFound_404")
    void deleteEmployee_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.deleteEmployee(testEmployee.getId()))
                .thenThrow(new ResourceNotFoundException("Employee with id: " + 1 + " doesn't exist"));

        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deleteEmployee(Long id)_unauthenticatedUser_401")
    void deleteEmployee_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}