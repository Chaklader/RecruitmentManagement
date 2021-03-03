package com.recruitment.manager.dto;



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

    private AddressDto addressDto;

}
