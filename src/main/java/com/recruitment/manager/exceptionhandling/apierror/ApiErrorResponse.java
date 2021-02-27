package com.recruitment.manager.exceptionhandling.apierror;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.recruitment.manager.exceptionhandling.LowerCaseClassNameResolver;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by Chaklader on Feb, 2021
 */
@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public
class ApiErrorResponse {


    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private String debugMessage;

    private List<ApiSubError> subErrors;


    private ApiErrorResponse() {

        timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(HttpStatus status) {

        this();
        this.status = status;
    }


    private void addSubError(ApiSubError subError) {

        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {

        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {

        addSubError(new ApiValidationError(object, message));
    }

    private void addValidationError(FieldError fieldError) {

        this.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {

        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {

        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {

        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated
     * validation fails.
     */
    private void addValidationError(ConstraintViolation<?> constraintViolation) {

        this.addValidationError(
            constraintViolation.getRootBeanClass().getSimpleName(),
            ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().asString(),
            constraintViolation.getInvalidValue(),
            constraintViolation.getMessage());
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {

        constraintViolations.forEach(this::addValidationError);
    }


}

