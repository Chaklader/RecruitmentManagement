package com.recruitment.manager.util;


/**
 * Created by Chaklader on Feb, 2021
 */
public class RegexConstants {


    public static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    public static final String EMAIL_PATTERN = ".+@.+\\..+";

    public static final String PHONE_NUMBER_PATTERN = "\\(?\\d{3}\\)?-? *\\d{3}-? *-?\\d{4}";

    public static final String POSTAL_CODE_PATTERN = "^[0-9]{5}(?:-[0-9]{4})?$";
}