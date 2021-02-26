package com.recruitment.manager.api;

import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.entity.EmployeeDto;
import com.recruitment.manager.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class EmployeeController {


    /*
        - An Endpoint to support adding an employee with very basic employee details including
        (name, contract information, age, you can decide.) With initial state "ADDED" which incidates
        that the employee isn't active yet.

        - Another endpoint to change the state of a given employee to "In-CHECK" or any of the states
        defined above in the state machine
    * */

    @Autowired
    private EmployeeService employeeService;


    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody EmployeeDto user) {

        Employee employee = employeeService.create(user);

        return new ResponseEntity<>(employee, new HttpHeaders(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> findAllUsers() {

        List<Employee> all = employeeService.findAll();

        return new ResponseEntity<>(all, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findUserById(@PathVariable(value = "id") long id) {

        Optional<Employee> user = employeeService.findById(id);

        ResponseEntity<Employee> entity = user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());

        return entity;
    }


}