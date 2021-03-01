package com.recruitment.manager.repo;

import com.recruitment.manager.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}