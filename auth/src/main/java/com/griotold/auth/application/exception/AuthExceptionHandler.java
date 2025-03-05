package com.griotold.auth.application.exception;

import com.griotold.common.exception.ExceptionResponse;
import com.griotold.common.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler extends GlobalExceptionHandler {
    private final String ERROR_LOG = "[ERROR] %s %s";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ExceptionResponse handleBindException(final BindException e) {
        log.error(String.format(ERROR_LOG, e.getObjectName(), e.getFieldErrors()));
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ExceptionResponse(errorMessage);
    }
}
