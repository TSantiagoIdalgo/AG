package com.ancore.ancoregaming.common;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptions {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleEntityNotFoundException(EntityNotFoundException ex) {
    Map<String, String> errors = new HashMap<>();

    if (ex.getCause() != null) {
      errors.put(ex.getMessage(), ex.getCause().getMessage());
    } else {
      errors.put("message", ex.getMessage());
    }
    ApiResponse<Map<String, String>> response = new ApiResponse<>(ResponseMessage.NOT_FOUND, null, errors);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiResponse<String>> handleBadRequestException(BadRequestException ex) {
    Map<String, String> errors = new HashMap<>();
    if (ex.getCause() != null) {
      errors.put(ex.getMessage(), ex.getCause().getMessage());
    } else {
      errors.put("message", ex.getMessage());
    }
    ApiResponse<String> response = new ApiResponse<>(ResponseMessage.BAD_REQUEST, null, errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ApiResponse<Map<String, String>> response = new ApiResponse<>(ResponseMessage.INVALID_ARGUMENTS, null, errors);
    return ResponseEntity.status(406).body(response);
  }
}
