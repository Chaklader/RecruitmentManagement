package com.recruitment.manager.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recruitment.manager.enums.EmployeeStates;
import lombok.Data;

/**
 * Created by Chaklader on Feb, 2021
 */
@Data
public class EmployeeDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Integer age;

//    @JsonIgnore
//    private EmployeeStates statee;

    private AddressDto addressDto;

}
