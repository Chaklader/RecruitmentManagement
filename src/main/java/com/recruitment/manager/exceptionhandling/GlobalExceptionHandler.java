package com.recruitment.manager.exceptionhandling;


import com.recruitment.manager.exceptionhandling.apierror.ApiErrorResponse;
import com.recruitment.manager.util.ApiResponseMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


/**
 * Created by Chaklader on Feb, 2021
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


//    @Override
//    @NonNull
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//        @NonNull MethodArgumentNotValidException ex,
//        @NonNull HttpHeaders headers,
//        @NonNull HttpStatus status,
//        @NonNull WebRequest request
//
//    ) {
//
//        String message = prepareMessageFromException(ex, (ServletWebRequest) request);
//
//        ApiErrorResponse apiError = new ApiErrorResponse(BAD_REQUEST);
//        apiError.setMessage("Validation error");
//        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
//        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
//        return buildResponseEntity(apiError);
//    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {

        ApiErrorResponse apiError = new ApiErrorResponse(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        ServletWebRequest req = (ServletWebRequest) request;
        String message = prepareMessageFromException(ex, (ServletWebRequest) request);

        log.info("{} to {}", req.getHttpMethod(), req.getRequest().getServletPath());
        log.error(message, ex);

        return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST,
            message), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        @Nonnull HttpMessageNotReadableException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatus status,
        @Nonnull WebRequest request
    ) {

        String message = prepareMessageFromException(ex, (ServletWebRequest) request);

        return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST,
            message), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private String prepareMessageFromException(Exception ex, ServletWebRequest request) {

        String message = ex.getMessage();

        log.info("{} to {}", request.getHttpMethod(), request.getRequest().getServletPath());
        log.error(message);

        if (message != null && !message.isEmpty()) {
            message = message.split(":")[0];
        }

        return message;
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
        @Nonnull HttpMessageNotWritableException ex,
        @Nonnull HttpHeaders headers,
        @Nonnull HttpStatus status,
        @Nonnull WebRequest request
    ) {

        String error = "Error writing JSON output";

        return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error. please contact support !!"), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {

        return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.NOT_FOUND,
            "Resource not found: "), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.NOT_FOUND,
//            errors.toString()), new HttpHeaders(), HttpStatus.NOT_FOUND);
//    }


//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
//
//        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

    @Nonnull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                                     .getBindingResult()
                                     .getFieldErrors()
                                     .stream()
                                     .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                     .collect(Collectors.toList());

        ErrorDetails errorDetails = new ErrorDetails(BAD_REQUEST, ex.getLocalizedMessage(), errorList);

        return handleExceptionInternal(ex, errorDetails, headers, errorDetails.getStatus(), request);
    }

    @Data
    private static class ErrorDetails {

        private HttpStatus status;
        private String message;
        private List<String> errors;

        public ErrorDetails(HttpStatus status, String message, List<String> errors) {
            super();
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

        public ErrorDetails(HttpStatus status, String message, String error) {
            super();
            this.status = status;
            this.message = message;
            errors = Arrays.asList(error);
        }
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {

        if (ex.getCause() instanceof ConstraintViolationException) {
            return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.CONFLICT,
                "Database error"), new HttpHeaders(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error. please contact support !!" + ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {

        return new ResponseEntity<>(ApiResponseMessage.getGenericApiResponse(Boolean.FALSE, HttpStatus.BAD_REQUEST,
            String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType())), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
