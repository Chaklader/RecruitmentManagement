package com.recruitment.manager.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recruitment.manager.validator.ValidZip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * Created by Chaklader on Feb, 2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "address")
@Table(name = "address")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "street")
    private String street;

    @NotBlank
    @Column(name = "state")
    private String state;

    @NotBlank
    @Column(name = "country")
    private String country;

    @ValidZip
    @Column(name = "zip")
    private String zip;

    @OneToOne(mappedBy = "address")
    private Employee employee;

}
