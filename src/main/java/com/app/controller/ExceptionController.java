package com.app.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.app.dto.ApiError;
import com.app.exception.InvalidInputException;
import com.app.exception.ServerError;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiError> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ApiError(null, e.getMessage(), null));
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ApiError(null, "The resource that you're trying to access is not found", e.getMessage()));
    }

    @ExceptionHandler(ServerError.class)
    public ResponseEntity<ApiError> handleServerError(ServerError e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body(new ApiError(null, "Something went wrong", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiError(null, "Something went wrong", e.getMessage()));
    }
}
