package com.evoila.springsecuritytask.dto;


import com.evoila.springsecuritytask.model.Employee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class EmployeeMapperTest {


    @Test
    void convertToEmployeeDTO_shouldConvertEmployeeObjectToEmployeeDTO() {
        Employee testEmployee = new Employee(1L, "testFirstName", "testLastName", "testemail@email.com");

        EmployeeDTO expectedEmployeeDTO = EmployeeMapper.convertToEmployeeDTO(testEmployee);

        assertAll(
                () -> assertEquals(testEmployee.getFirstName(), expectedEmployeeDTO.getFirstName()),
                () -> assertEquals(testEmployee.getLastName(), expectedEmployeeDTO.getLastName()),
                () -> assertEquals(testEmployee.getEmail(), expectedEmployeeDTO.getEmail()));
    }

    @Test
    void convertToEmployee_shouldConvertEmployeeDTOObjectToEmployee() {
        EmployeeDTO testEmployeeDTO = EmployeeDTO.builder()
                .id(1L)
                .firstName("testFirstName")
                .lastName("testLastName")
                .email("testemail@email.com")
                .build();

        Employee expectedEmployee = EmployeeMapper.convertToEmployee(testEmployeeDTO);

        assertAll(
                () -> assertEquals(testEmployeeDTO.getFirstName(), expectedEmployee.getFirstName()),
                () -> assertEquals(testEmployeeDTO.getLastName(), expectedEmployee.getLastName()),
                () -> assertEquals(testEmployeeDTO.getEmail(), expectedEmployee.getEmail()));
    }
}