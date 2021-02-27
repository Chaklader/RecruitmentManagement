package com.recruitment.manager.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.recruitment.manager.util.RegexConstants.EMAIL_PATTERN;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotBlank
@Pattern(regexp = EMAIL_PATTERN, message = "Please provide a valid email address")
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidEmail {

    String message() default "Please provide a valid email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
