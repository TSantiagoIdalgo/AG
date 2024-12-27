package com.ancore.ancoregaming.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiEntityResponse<T> {
    private HttpStatus status;
    private ApiResponse<T> body;

    public ApiEntityResponse(HttpStatus status, ApiResponse<T> body) {
        this.status = status;
        this.body = body;
    }

    public static <T> ApiEntityResponse<T> of(HttpStatus status, ApiResponse<T> body) {
        return new ApiEntityResponse<>(status, body);
    }
}
