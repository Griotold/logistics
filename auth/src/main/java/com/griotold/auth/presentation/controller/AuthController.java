package com.griotold.auth.presentation.controller;

import com.griotold.auth.application.dto.UserResponse;
import com.griotold.auth.application.service.AuthService;
import com.griotold.auth.presentation.dto.UserSignInRequest;
import com.griotold.auth.presentation.dto.UserSignUpRequest;
import com.griotold.common.presentation.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody UserSignUpRequest signUpRequest) {
        log.info("SignUp.UserSignUpRequest : {}", signUpRequest);
        UserResponse userResponse = authService.singUp(signUpRequest.toServiceDto());
        return ApiResponse.success(userResponse);
    }

    @PostMapping("/signIn")
    public ApiResponse<String> signIn(@Valid @RequestBody UserSignInRequest signInRequest) {
        log.info("SignIn.UserSignInRequest : {}", signInRequest);
        String token = authService.singIn(signInRequest.toServiceDto());
        return ApiResponse.success(token);
    }

    @GetMapping("/verify/{userId}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable("userId") Long userId) {
        log.info("verifyUser userId : {}", userId);
        return ResponseEntity.ok(authService.verifyUser(userId));
    }
}
