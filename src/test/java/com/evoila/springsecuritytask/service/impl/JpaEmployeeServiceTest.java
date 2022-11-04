package com.evoila.springsecuritytask.service.impl;


import com.evoila.springsecuritytask.exception.ResourceNotFoundException;
import com.evoila.springsecuritytask.model.Employee;
import com.evoila.springsecuritytask.repository.EmployeeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class JpaEmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private JpaEmployeeService jpaEmployeeService;

    private final Employee testEmployee1 =
            new Employee(1L, "testFirstName1", "testLastName1", "testemail1@email.com");
    private final Employee testEmployee2 =
            new Employee(2L, "testFirstName2", "testLastName2", "testemail2@email.com");
    private final Employee testEmployee3 =
            new Employee(3L, "testFirstName3", "testLastName3", "testemail3@email.com");


    @Test
    void getEmployees_shouldReturnAllEmployees() {
        List<Employee> employees = new ArrayList<>(Arrays.asList(testEmployee1, testEmployee2, testEmployee3));

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> expectedEmployeesList = jpaEmployeeService.getEmployees();

        assertEquals(expectedEmployeesList, employees);
        Mockito.verify(employeeRepository).findAll();
    }


    @Test
    void getEmployeeById_whenGivenEmployeeId_shouldReturnEmployeeById_ifEmployeeExists() {
        Mockito.when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));

        Employee expectedEmployee = jpaEmployeeService.getEmployeeById(1L);

        assertEquals(expectedEmployee, testEmployee1);
        Mockito.verify(employeeRepository).findById(1L);
    }

    @Test
    void getEmployeeById_whenGivenEmployeeId_shouldThrowResourceNotFoundException_ifEmployeeDoesNotExist() {
        Mockito.when(employeeRepository.findById(25L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jpaEmployeeService.getEmployeeById(25L));
    }

    @Test
    void createEmployee_whenGivenEmployee_shouldCreateAndReturnGivenEmployee() {
        Mockito.when(employeeRepository.save(testEmployee1)).thenReturn(testEmployee1);

        Employee createdEmployee = jpaEmployeeService.createEmployee(testEmployee1);

        assertEquals(createdEmployee, testEmployee1);
        Mockito.verify(employeeRepository).save(testEmployee1);
    }

    @Test
    void updateEmployee_whenGivenEmployeeIdAndEmployee_shouldUpdateAndReturnUpdatedEmployee_ifEmployeeExists() {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("newFirstName");
        newEmployee.setLastName("newLastName");
        newEmployee.setEmail("newemail@email.com");

        Mockito.when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));

        jpaEmployeeService.updateEmployee(1L, newEmployee);

        Mockito.verify(employeeRepository).findById(testEmployee1.getId());
        Mockito.verify(employeeRepository).save(testEmployee1);
    }

    @Test
    void updateEmployee_whenGivenEmployeeIdAndEmployee_shouldThrowResourceNotFoundException_ifEmployeeDoesNotExist() {
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("newFirstName");
        newEmployee.setLastName("newLastName");
        newEmployee.setEmail("newemail@email.com");

        Mockito.when(employeeRepository.findById(25L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jpaEmployeeService.updateEmployee(25L, newEmployee));
    }

    @Test
     void deleteEmployee_whenGivenEmployeeId_shouldDeleteEmployee_ifEmployeeExists(){
        Mockito.when(employeeRepository.findById(testEmployee1.getId())).thenReturn(Optional.of(testEmployee1));

        boolean isEmployeeDeleted = jpaEmployeeService.deleteEmployee(testEmployee1.getId());

        assertTrue(isEmployeeDeleted);
        Mockito.verify(employeeRepository).delete(testEmployee1);
    }

    @Test
    void deleteEmployee_whenGivenEmployeeId_shouldThrowResourceNotFoundException_ifEmployeeDoesNotExist() {
        Mockito.when(employeeRepository.findById(25L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jpaEmployeeService.deleteEmployee(25L));
    }
}