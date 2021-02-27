package com.recruitment.manager.util;

import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.*;



/**
 * Created by Chaklader on Feb, 2021
 */
public class ApiResponseMessage {


    static final String TAG_STATUS = "status";

    static final String TAG_IS_SUCCESSFUL="isSuccessful";

    static Map<String, Object> apiResponse;


    public static Map<String, Object> getGenericApiResponse(Boolean isSuccessful, HttpStatus httpStatusCode, String message) {

        apiResponse = new LinkedHashMap<>();

        apiResponse.put(TAG_IS_SUCCESSFUL, isSuccessful);
        apiResponse.put(TAG_STATUS, httpStatusCode.value());
        apiResponse.put("message", message);

        return apiResponse;
    }


    public static Map<String, Object> getNotFoundApiResponse(String message) {

        apiResponse = new LinkedHashMap<>();

        apiResponse.put(TAG_IS_SUCCESSFUL, Boolean.FALSE);
        apiResponse.put(TAG_STATUS, HttpStatus.NOT_FOUND.value());
        apiResponse.put("message", "Resource not found " + message);

        return apiResponse;
    }

    public static Map<String, Object> getInternalServerError() {

        apiResponse = new LinkedHashMap<>();

        apiResponse.put(TAG_IS_SUCCESSFUL, Boolean.FALSE);
        apiResponse.put(TAG_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiResponse.put("message", "Internal server error. please contact support !!");

        return apiResponse;
    }

    public static Map<String, Object> getBuildValidationErrors(Set<ConstraintViolation<Object>> validateErrors) {

        List<Map<Object, Object>> errorsList = new ArrayList<>();
        apiResponse = new LinkedHashMap<>();

        for (ConstraintViolation<Object> error : validateErrors) {

            LinkedHashMap<Object, Object> errorMap = new LinkedHashMap<Object, Object>();
            errorMap.put("field", error.getPropertyPath().toString());
            errorMap.put("message", error.getMessage());

            errorsList.add(errorMap);
        }

        apiResponse.put("name", "ValidationError");
        apiResponse.put(TAG_STATUS, HttpStatus.BAD_REQUEST.value());
        apiResponse.put("details", errorsList);

        return apiResponse;
    }
}
