package com.recruitment.manager.dto;

import lombok.Data;

/**
 * Created by Chaklader on Feb, 2021
 */
@Data
public class AddressDto {


    private String street;

    private String state;

    private String country;

    private int zip;
}
