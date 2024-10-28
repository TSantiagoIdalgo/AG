package com.ancore.ancoregaming.common;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptions {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<ExceptionResponse>> handleGeneralException(Exception ex, WebRequest request) {
    ExceptionResponse error = new ExceptionResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getMessage(),
            "General Error"
    );
    ApiResponse response = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResponse<ExceptionResponse>> handleEntityNotFoundException(EntityNotFoundException ex) {
    ExceptionResponse error = new ExceptionResponse(404, ex.getMessage(), "NOT_FOUND");
    ApiResponse response = new ApiResponse<>(HttpStatus.NOT_FOUND, null, error);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiResponse<String>> handleBadRequestException(BadRequestException ex) {
    ExceptionResponse error = new ExceptionResponse(404, ex.getMessage(), "BAD_REQUEST");
    ApiResponse response = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ExceptionResponse error = new ExceptionResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            errors.toString()
    );
    ApiResponse response = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<ExceptionResponse>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    ExceptionResponse error = new ExceptionResponse(
            HttpStatus.CONFLICT.value(),
            "Data Integrity Violation",
            ex.getRootCause().getMessage()
    );

    ApiResponse response = new ApiResponse<>(HttpStatus.CONFLICT, null, error);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<ExceptionResponse>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    String message = String.format("The value '%s' is not valid for parameter '%s'",
            ex.getValue(), ex.getName());

    ExceptionResponse error = new ExceptionResponse(
            HttpStatus.BAD_REQUEST.value(),
            message,
            "Type Mismatch Error"
    );

    ApiResponse response = new ApiResponse<>(HttpStatus.BAD_REQUEST, null, error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
