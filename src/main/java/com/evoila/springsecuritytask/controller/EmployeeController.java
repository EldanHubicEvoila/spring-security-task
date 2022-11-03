package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private com.evoila.springsecuritytask.service.impl.JPAEmployeeService JPAEmployeeService;


    @GetMapping()
    public ResponseEntity<List<Employee>> getEmployees() {
        return new ResponseEntity<>(JPAEmployeeService.getEmployees(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(JPAEmployeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<>(JPAEmployeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,@Valid @RequestBody Employee employeeDetails) {
        return new ResponseEntity<>(JPAEmployeeService.updateEmployee(id, employeeDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id){
        return new ResponseEntity<>(JPAEmployeeService.deleteEmployee(id));
    }
}
