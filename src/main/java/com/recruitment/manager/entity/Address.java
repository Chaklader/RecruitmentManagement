package com.recruitment.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;




/**
 * Created by Chaklader on Feb, 2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "address")
@Table(name = "address")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;


    @Column(name = "street")
    private String street;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zip")
    private int zip;


    @OneToOne(mappedBy = "address")
    private Employee employee;

}
