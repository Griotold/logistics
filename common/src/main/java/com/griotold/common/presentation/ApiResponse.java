package com.griotold.common.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T> (
        HttpStatus status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "API 요청에 성공했습니다", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(HttpStatus.OK,"API 요청에 성공했습니다", null);
    }
}