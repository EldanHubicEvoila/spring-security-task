package com.evoila.springsecuritytask.service;

import com.evoila.springsecuritytask.model.Employee;
import org.springframework.http.HttpStatus;

import java.util.List;


public interface EmployeeService {

    List<Employee> getEmployees();
    Employee createEmployee(Employee employee);
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employee);
    boolean deleteEmployee(Long id);
}
