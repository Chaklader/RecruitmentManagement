package com.recruitment.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.recruitment.manager.validator.ValidZip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


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
    private UUID uuid;

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

    @Transient
    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private Employee employee;

}
