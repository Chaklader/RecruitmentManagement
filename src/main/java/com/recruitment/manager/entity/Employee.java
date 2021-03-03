package com.recruitment.manager.entity;

import com.recruitment.manager.enums.EmployeeStates;
import com.recruitment.manager.validator.ValidEmail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.recruitment.manager.validator.ValidPhoneNumber;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "Employee")
@Table(name = "employee")
public class Employee {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_on" ,updatable = false)
    private Date creationOn;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @ValidEmail(message = "provide a valid email address")
    @Column(name = "email")
    private String email;

    @ValidPhoneNumber(message = "provide a valid phone number")
    @Column(name = "phoneNumber")
    private String phoneNumber;


    @Min(value = 18, message = "an employee need to be at-least 18 years to start working")
    @Positive
    @Column(name = "age")
    private Integer age;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EmployeeStates employeeState;


    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;


}