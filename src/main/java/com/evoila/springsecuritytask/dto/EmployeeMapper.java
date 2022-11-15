package com.evoila.springsecuritytask.dto;

import com.evoila.springsecuritytask.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public static EmployeeDTO convertToEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
    }

    public static Employee convertToEmployee(EmployeeDTO employeeDTO) {
        return new Employee(employeeDTO.getId(),
                            employeeDTO.getFirstName(),
                            employeeDTO.getLastName(),
                            employeeDTO.getEmail());
    }
}
