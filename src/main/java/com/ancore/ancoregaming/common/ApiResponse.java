package com.ancore.ancoregaming.common;

import java.util.Map;

public class ApiResponse<T> {

  private ResponseMessage responseMsg;
  private T data;
  private Map<String, String> errors;

  public ApiResponse(ResponseMessage responseMsg, T data, Map<String, String> errors) {
    this.responseMsg = responseMsg;
    this.data = data;
    this.errors = errors;
  }

  public ResponseMessage getResponseMsg() {
    return responseMsg;
  }

  public void setResponseMsg(ResponseMessage responseMsg) {
    this.responseMsg = responseMsg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public void setErrors(Map<String, String> errors) {
    this.errors = errors;
  }

}
