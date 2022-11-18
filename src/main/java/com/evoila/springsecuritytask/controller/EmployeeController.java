package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.dto.EmployeeDTO;
import com.evoila.springsecuritytask.dto.EmployeeMapper;
import com.evoila.springsecuritytask.model.Employee;
import com.evoila.springsecuritytask.service.EmployeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping()
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        return new ResponseEntity<>(employeeService.getEmployees()
                .stream()
                .map(EmployeeMapper::convertToEmployeeDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.convertToEmployee(employeeDTO);
        EmployeeDTO createdEmployee = EmployeeMapper.convertToEmployeeDTO(employeeService.createEmployee(employee));

        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<>(EmployeeMapper.convertToEmployeeDTO(employeeService.getEmployeeById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employeeDetails = EmployeeMapper.convertToEmployee(employeeDTO);
        EmployeeDTO updatedEmployee = EmployeeMapper.convertToEmployeeDTO(employeeService.updateEmployee(id, employeeDetails));

        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.deleteEmployee(id), HttpStatus.OK);
    }
}
