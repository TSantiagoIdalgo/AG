package com.ancore.ancoregaming.common;

import org.springframework.http.HttpStatus;

public class ApiEntityResponse<T> {
    private HttpStatus status;
    private ApiResponse<T> body;

    public ApiEntityResponse(HttpStatus status, ApiResponse<T> body) {
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ApiResponse<T> getBody() {
        return body;
    }

    public void setBody(ApiResponse<T> body) {
        this.body = body;
    }

    public static <T> ApiEntityResponse<T> of(HttpStatus status, ApiResponse<T> body) {
        return new ApiEntityResponse<>(status, body);
    }
}
