package com.recruitment.manager.api;

import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.dto.EmployeeDto;
import com.recruitment.manager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/employee")
public class EmployeeController {


    /*
        - An Endpoint to support adding an employee with very basic employee details including
        (name, contract information, age, you can decide.) With initial state "ADDED" which incidates
        that the employee isn't active yet.

        - Another endpoint to change the state of a given employee to "In-CHECK" or any of the states
        defined above in the state machine
    * */

    // TODO
    /*
      1. Improve the models and add the model validations with the email regex etc
      2. Add the global exceptions
      3. Improve the API code and write them properly

      4. Add docker: Being simply executable with the least effort Ideally using
         Docker and docker-compose or any similar approach.
      5. Write the IT tests using the rest assured
      6. Write the commend for the building the state machine
      7. Write the README file for the app
    * */

    @Autowired
    private EmployeeService employeeService;


    @PostMapping(value = "/create")
    public ResponseEntity<Object> createEmployee(@Validated @RequestBody EmployeeDto employeeDto) {

        Employee employee = employeeService.createEmployee(employeeDto);

        return new ResponseEntity<>(employee, new HttpHeaders(), HttpStatus.CREATED);
    }


    @PutMapping(value = "incheck/{id}")
    public ResponseEntity<Object> changeStateInCheck(@PathVariable(value = "id") long id) {

        Employee employee = employeeService.changeToInCheckState(id, "");

        return new ResponseEntity<>(employee, new HttpHeaders(), HttpStatus.OK);

    }

}