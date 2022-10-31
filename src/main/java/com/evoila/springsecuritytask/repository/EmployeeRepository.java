package com.evoila.springsecuritytask.repository;

import com.evoila.springsecuritytask.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
