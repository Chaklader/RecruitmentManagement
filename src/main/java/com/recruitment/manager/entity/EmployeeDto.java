package com.recruitment.manager.entity;

import com.recruitment.manager.enums.EmployeeStates;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * Created by Chaklader on Feb, 2021
 */
@Data
public class EmployeeDto {

    private String firstName;

    private String lastName;

    private String email;

//    private String state;
}
