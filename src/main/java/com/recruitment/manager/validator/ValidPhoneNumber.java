package com.recruitment.manager.validator;

import com.recruitment.manager.util.RegexConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Chaklader on Feb, 2021
 */
@NotBlank
@Pattern(regexp = RegexConstants.PHONE_NUMBER_PATTERN, message = "Please provide a valid phone number")
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidPhoneNumber {

    String message() default "Please provide a valid phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
