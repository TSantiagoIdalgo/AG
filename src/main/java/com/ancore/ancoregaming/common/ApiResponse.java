package com.ancore.ancoregaming.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
  private T data;
  private ExceptionResponse error;

  public ApiResponse() {
  }

  public ApiResponse(T data, ExceptionResponse error) {
    this.data = data;
    this.error = error;
  }

}
