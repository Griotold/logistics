package com.griotold.auth.presentation.controller;

import com.griotold.auth.application.dto.UserResponse;
import com.griotold.auth.application.service.AuthService;
import com.griotold.auth.presentation.dto.UserSignUpRequest;
import com.griotold.common.presentation.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    // todo RequestLoggingAspect 추가하기

    @PostMapping("/signUp")
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody UserSignUpRequest signUpRequest) {
        UserResponse userResponse = authService.singUp(signUpRequest.toServiceDto());
        return ApiResponse.success(userResponse);
    }
}
