package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.dto.EmployeeDTO;
import com.evoila.springsecuritytask.dto.EmployeeMapper;
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
class EmployeeSecureControllerIntegrationTest extends AbstractSecureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    EmployeeDTO testEmployeeDTO = EmployeeDTO.builder()
            .id(1L)
            .firstName("testFirstName")
            .lastName("testLastName")
            .email("testemail@email.com")
            .build();


    @Test
    @WithMockUser
    @DisplayName("getEmployees()_200")
    void getEmployees_shouldReturnAllEmployees_200() throws Exception {
        List<Employee> expectedEmployees = new ArrayList<>(List.of(EmployeeMapper.convertToEmployee(testEmployeeDTO)));

        when(employeeService.getEmployees())
                .thenReturn(expectedEmployees);

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value(testEmployeeDTO.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(testEmployeeDTO.getLastName()))
                .andExpect(jsonPath("$[0].email").value(testEmployeeDTO.getEmail()));
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
    @DisplayName("createEmployee(EmployeeDTO employeeDTO)_201")
    void createEmployee_whenValidBody_shouldCreateAndReturnNewEmployee_201() throws Exception {
        when(employeeService.createEmployee(any(Employee.class)))
                .thenReturn(EmployeeMapper.convertToEmployee(testEmployeeDTO));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(testEmployeeDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployeeDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployeeDTO.getEmail()));
    }

    @Test
    @WithMockUser
    @DisplayName("createEmployee(EmployeeDTO employeeDTO)_fieldNull_400")
    void createEmployee_whenFieldNull_shouldReturnBadRequest_400() throws Exception {
        testEmployeeDTO.setFirstName(null);

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(EmployeeMapper.convertToEmployee(testEmployeeDTO));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("createEmployee(EmployeeDTO employeeDTO)_badEmailFormat_400")
    void createEmployee_whenBadEmailFormat_shouldReturnBadRequest_400() throws Exception {
        testEmployeeDTO.setEmail("badEmailFormat");

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(EmployeeMapper.convertToEmployee(testEmployeeDTO));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("createEmployee(EmployeeDTO employeeDTO)_unauthenticatedUser_401")
    void createEmployee_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("getEmployeeById(Long id)_200")
    void getEmployeeById_whenEmployeeExists_shouldGetEmployeeById_200() throws Exception {
        when(employeeService.getEmployeeById(testEmployeeDTO.getId()))
                .thenReturn(EmployeeMapper.convertToEmployee(testEmployeeDTO));

        mockMvc.perform(get("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployeeDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployeeDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployeeDTO.getEmail()));
    }

    @Test
    @WithMockUser
    @DisplayName("getEmployeeById(Long id)_noEmployeeFound_404")
    void getEmployeeById_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.getEmployeeById(testEmployeeDTO.getId()))
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
    @DisplayName("updateEmployee(Long id, EmployeeDTO employeeDTO)_200")
    void updateEmployee_shouldUpdateExistingEmployee_200() throws Exception {
        when(employeeService.updateEmployee(eq(testEmployeeDTO.getId()), any(Employee.class)))
                .thenReturn(EmployeeMapper.convertToEmployee(testEmployeeDTO));

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployeeDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployeeDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployeeDTO.getEmail()));
    }

    @Test
    @WithMockUser
    @DisplayName("updateEmployee(Long id, EmployeeDTO employeeDTO)_fieldNull_400")
    void updateEmployee_whenFieldNull_shouldReturnBadRequest_400() throws Exception {
        testEmployeeDTO.setEmail(null);

        when(employeeService.updateEmployee(eq(testEmployeeDTO.getId()), any(Employee.class)))
                .thenReturn(EmployeeMapper.convertToEmployee(testEmployeeDTO));

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("updateEmployee(Long id, EmployeeDTO employeeDTO)_badEmailFormat_400")
    void updateEmployee_whenBadEmailFormat_shouldReturnBadRequest_400() throws Exception {
        testEmployeeDTO.setEmail("badEmailFormat");

        when(employeeService.updateEmployee(eq(testEmployeeDTO.getId()), any(Employee.class)))
                .thenReturn(EmployeeMapper.convertToEmployee(testEmployeeDTO));

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("updateEmployee(Long id, EmployeeDTO employeeDTO)_noEmployeeFound_404")
    void updateEmployee_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.updateEmployee(eq(testEmployeeDTO.getId()), any(Employee.class)))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateEmployee(Long id, Employee employeeDetails)_unauthenticatedUser_401")
    void updateEmployee_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(put("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployeeDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteEmployee(Long id)_200")
    void deleteEmployee_shouldDeleteExistingEmployee_200() throws Exception {
        when(employeeService.deleteEmployee(testEmployeeDTO.getId()))
                .thenReturn(true);

        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteEmployee(Long id)_noEmployeeFound_404")
    void deleteEmployee_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.deleteEmployee(testEmployeeDTO.getId()))
                .thenThrow(new ResourceNotFoundException("Employee with id: " + 1 + " doesn't exist"));

        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deleteEmployee(Long id)_unauthenticatedUser_401")
    void deleteEmployee_whenUnauthenticated_shouldReturnUnauthorized_401() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}