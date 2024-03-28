package com.app.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import com.app.dto.ApiError;
import com.app.exception.InvalidInputException;
import com.app.exception.ServerError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class ExceptionController {

	 @ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
	 			
	    		System.out.println("ex.getBindingResult().getAllErrors()-------"+ex.getBindingResult().getAllErrors());
	 			Map<String, String> errors = new HashMap<>();
	 			ex.getBindingResult().getAllErrors().forEach((error) ->{
	 				
	 				String fieldName = ((FieldError) error).getField();
	 				String message = error.getDefaultMessage();
	 				errors.put(fieldName, message);
	 			});
	 	   return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                          .body(new ApiError(null, errors.toString()));
	 			

	 }
	   
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiError> handleExpiredJwtException (JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ApiError(e.getMessage(), "Json To String Error"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ApiError(null, e.getMessage()));
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidInputException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ApiError(null, e.getMessage()));
    }
	
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiError> handleInvalidInputException(InvalidInputException e) {
    	System.out.println("INSIDE ExceptionController*************");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ApiError(null, e.getMessage()));
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ApiError(e.getMessage(), "The resource that you're trying to access is not found"));
    }

    @ExceptionHandler(ServerError.class)
    public ResponseEntity<ApiError> handleServerError(ServerError e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body(new ApiError(e.getMessage(), "Something went wrong"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiError(e.getMessage(), "Something went wrong"));
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiError(e.getMessage(), "Null Pointer Exception"));
    }
    
    @ExceptionHandler(FirebaseMessagingException.class)
    public ResponseEntity<ApiError> handleFirebaseMessagingException(FirebaseMessagingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiError(e.getMessage(), "Something went wrong"));
    }

}
