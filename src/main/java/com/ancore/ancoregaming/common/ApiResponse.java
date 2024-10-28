package com.ancore.ancoregaming.common;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

  private HttpStatus responseMsg;
  private T data;
  private ExceptionResponse error;

  public ApiResponse(HttpStatus responseMsg, T data, ExceptionResponse error) {
    this.responseMsg = responseMsg;
    this.data = data;
    this.error = error;
  }

  public HttpStatus getResponseMsg() {
    return responseMsg;
  }

  public void setResponseMsg(HttpStatus responseMsg) {
    this.responseMsg = responseMsg;
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
