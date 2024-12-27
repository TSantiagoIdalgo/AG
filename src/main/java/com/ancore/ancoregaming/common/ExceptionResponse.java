package com.ancore.ancoregaming.common;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {

  private int status;
  private String message;
  private String error;
  private LocalDateTime timestamp;

  // Constructor, Getters y Setters
  public ExceptionResponse(int status, String message, String error) {
    this.status = status;
    this.message = message;
    this.error = error;
    this.timestamp = LocalDateTime.now();
  }
}
