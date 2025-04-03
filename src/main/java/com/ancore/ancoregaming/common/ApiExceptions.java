package com.ancore.ancoregaming.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ApiExceptions {

  @ExceptionHandler(Exception.class)
  public ApiEntityResponse<ExceptionResponse> handleGeneralException(Exception ex, WebRequest request) {
    ExceptionResponse error = new ExceptionResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ex.getMessage(),
        "INPUT_ERROR");
    ApiResponse<ExceptionResponse> response = new ApiResponse<>(null, error);
    return ApiEntityResponse.of(HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ApiEntityResponse<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
    ExceptionResponse error = new ExceptionResponse(404, ex.getMessage(), "NOT_FOUND");
    ApiResponse<ExceptionResponse> response = new ApiResponse<>(null, error);
    return ApiEntityResponse.of(HttpStatus.NOT_FOUND, response);
  }

  @ExceptionHandler(BadRequestException.class)
  public ApiEntityResponse<String> handleBadRequestException(BadRequestException ex) {
    ExceptionResponse error = new ExceptionResponse(400, ex.getMessage(), "BAD_REQUEST");
    ApiResponse<String> response = new ApiResponse<>(null, error);
    return ApiEntityResponse.of(HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiEntityResponse<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ExceptionResponse error = new ExceptionResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Validation Failed",
        errors.toString());
    ApiResponse<?> response = new ApiResponse<>(null, error);
    return ApiEntityResponse.of(HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ApiEntityResponse<ExceptionResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    ExceptionResponse error = new ExceptionResponse(
        HttpStatus.CONFLICT.value(),
        "Data Integrity Violation",
        ex.getMessage());

    ApiResponse<ExceptionResponse> response = new ApiResponse<>(null, error);
    return ApiEntityResponse.of(HttpStatus.CONFLICT, response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ApiEntityResponse<ExceptionResponse> handleTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    String message = String.format("The value '%s' is not valid for parameter '%s'",
        ex.getValue(), ex.getName());

    ExceptionResponse error = new ExceptionResponse(
        HttpStatus.BAD_REQUEST.value(),
        message,
        "Type Mismatch Error");

    ApiResponse<ExceptionResponse> response = new ApiResponse<>(null, error);
    return ApiEntityResponse.of(HttpStatus.BAD_REQUEST, response);
  }
}
