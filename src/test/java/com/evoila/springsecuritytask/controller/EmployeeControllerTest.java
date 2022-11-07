package com.evoila.springsecuritytask.controller;

import com.evoila.springsecuritytask.exception.ResourceNotFoundException;
import com.evoila.springsecuritytask.model.Employee;
import com.evoila.springsecuritytask.service.EmployeeService;
import com.evoila.springsecuritytask.util.JsonUtil;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private final Employee testEmployee =
            new Employee(1L, "testFirstName", "testLastName", "testemail@email.com");


    @Test
    @WithMockUser
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
    @WithMockUser
    void createEmployee_shouldCreateNewEmployee_201() throws Exception {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("testFirstName");
        testEmployee.setLastName("testLastName");
        testEmployee.setEmail("testEmail@email.com");


        when(employeeService.createEmployee(any(Employee.class)))
                .thenReturn(testEmployee);

        mockMvc.perform(post("/api/v1/employees").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployee.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));
    }

    @Test
    @WithMockUser
    void getEmployeeById_whenEmployeeExists_shouldGetEmployeeById_200() throws Exception {
        when(employeeService.getEmployeeById(testEmployee.getId()))
                .thenReturn(testEmployee);

        mockMvc.perform(get("/api/v1/employees/{id}", testEmployee.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void getEmployeeById_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.getEmployeeById(testEmployee.getId()))
                .thenThrow(new ResourceNotFoundException("Employee with id: " + 1 + " doesn't exist"));

        mockMvc.perform(get("/api/v1/employees/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateEmployee_shouldUpdateExistingEmployee_200() throws Exception {
        when(employeeService.updateEmployee(testEmployee.getId(), testEmployee))
                .thenReturn(testEmployee);

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testEmployee.getLastName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));;
    }

    @Test
    @WithMockUser
    public void updateEmployee_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.updateEmployee(testEmployee.getId(), testEmployee))
                .thenThrow(new ResourceNotFoundException("Employee with id: " + testEmployee.getId() + " doesn't exist"));

        mockMvc.perform(put("/api/v1/employees/{id}", testEmployee.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(testEmployee)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteEmployee_shouldDeleteExistingEmployee_200() throws Exception {
        when(employeeService.deleteEmployee(testEmployee.getId()))
                .thenReturn(true);

        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployee.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void deleteEmployee_whenEmployeeDoesNotExists_shouldThrowResourceNotFoundException_404() throws Exception {
        when(employeeService.deleteEmployee(testEmployee.getId()))
                .thenThrow(new ResourceNotFoundException("Employee with id: " + 1 + " doesn't exist"));

        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployee.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}