package com.recruitment.manager.entity;

import com.recruitment.manager.enums.EmployeeStates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Employee")
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "created_on")
    private Date creationOn;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EmployeeStates state;


    public Employee(String firstName, String lastName, String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.creationOn = new Date();
        this.email = email;

        this.state = EmployeeStates.ADDED;
    }
}