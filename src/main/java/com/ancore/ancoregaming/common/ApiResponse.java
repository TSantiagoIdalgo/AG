package com.ancore.ancoregaming.common;

public class ApiResponse<T> {
  private T data;
  private ExceptionResponse error;

  public ApiResponse() {
  }

  public ApiResponse(T data, ExceptionResponse error) {
    this.data = data;
    this.error = error;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public ExceptionResponse getError() {
    return error;
  }

  public void setError(ExceptionResponse error) {
    this.error = error;
  }

}
