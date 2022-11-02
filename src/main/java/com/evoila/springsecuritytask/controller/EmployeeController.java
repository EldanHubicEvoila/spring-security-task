package com.evoila.springsecuritytask.controller;


import com.evoila.springsecuritytask.model.Employee;
import com.evoila.springsecuritytask.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping()
    public ResponseEntity<List<Employee>> getEmployees() {
        return new ResponseEntity<>(employeeService.getEmployees(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        return new ResponseEntity<>(employeeService.updateEmployee(id, employeeDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id){
        return new ResponseEntity<>(employeeService.deleteEmployee(id));
    }
}
