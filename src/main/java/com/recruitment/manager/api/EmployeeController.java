package com.recruitment.manager.api;

import com.recruitment.manager.dto.EmployeeDto;
import com.recruitment.manager.entity.Employee;
import com.recruitment.manager.service.EmployeeService;
import com.recruitment.manager.util.ApiResponseMessage;
import com.recruitment.manager.util.MessageConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@Validated
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;


    /*
    * This Endpoint support adding an employee with very basic employee details including (name, contract information, age, you can decide.)
      With initial state "ADDED" which indicates that the employee isn't active yet. An example JSON for the Post request will be:

           {
                "firstName":"Bheng",
                "lastName":"Xyz",
                "email":"nmi.cher@gmail.com",
                "phoneNumber":"800-275-2273",
                "age":23,

                "addressDto":{

                    "street":"Zeltinger.25",
                    "state":"Berlin",
                    "country":"Germany",
                    "zip":12345
                }
            }
    *
    * */

    @Operation(summary = "create an employee in the recruiting platform")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Create employee using the dto", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))}),
        @ApiResponse(responseCode = "422", description = MessageConstant.EMPLOYEE_NOT_CREATE_MSG, content = @Content),
        @ApiResponse(responseCode = "500", description = MessageConstant.INTERNAL_SERVER_ERROR_MSG, content = @Content)})

    @PostMapping(value = "/create")
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {

        try {
            Employee employee = employeeService.createEmployee(employeeDto);

            if (employee != null) {

                return new ResponseEntity<>(employee, new HttpHeaders(), HttpStatus.CREATED);
            }

            return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.UNPROCESSABLE_ENTITY,
                MessageConstant.EMPLOYEE_NOT_CREATE_MSG), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception ex) {

            log.error(MessageConstant.INTERNAL_SERVER_ERROR_MSG + ex.getMessage());
            return new ResponseEntity<>(ApiResponseMessage.getInternalServerError(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
     * This endpoint to change the state of a given employee to "In-CHECK" or any of the states defined above in the state machine
     * */
    @Operation(description = "employee onboarding progress: change state from ADDED to IN_CHECK")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "change employee state to the IN_CHECK using employee Id", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))}),
        @ApiResponse(responseCode = "422", description = MessageConstant.EMPLOYEE_STATE_NOT_UPDATED_MSG, content = @Content),
        @ApiResponse(responseCode = "500", description = MessageConstant.INTERNAL_SERVER_ERROR_MSG, content = @Content)})

    @PutMapping(value = "/incheck/{id}")
    public ResponseEntity<Object> changeStateInCheck(@PathVariable(value = "id") long id) {

        try {
            Employee employee = employeeService.changeToInCheckState(id, "changing the employee state to the IN_CHECK");

            if (employee != null) {

                return new ResponseEntity<>(employee, new HttpHeaders(), HttpStatus.OK);
            }

            return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.UNPROCESSABLE_ENTITY,
                MessageConstant.EMPLOYEE_STATE_NOT_UPDATED_MSG), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception ex) {

            log.error(MessageConstant.INTERNAL_SERVER_ERROR_MSG + ex.getMessage());
            return new ResponseEntity<>(ApiResponseMessage.getInternalServerError(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}