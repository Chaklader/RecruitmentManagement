package com.recruitment.manager.validator;

import com.recruitment.manager.util.RegexConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Chaklader on Feb, 2021
 */
@NotBlank
@Pattern(regexp = RegexConstants.POSTAL_CODE_PATTERN, message = "Please provide a valid ZIP code")
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidZip {


    String message() default "Please provide a valid postal code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
