package com.griotold.auth.presentation.dto;

import com.griotold.auth.application.dto.UserSignIn;
import jakarta.validation.constraints.NotBlank;

public record UserSignInRequest(
        @NotBlank(message = "username 을 입력하세요.")
        String username,

        @NotBlank(message = "password 를 입력하세요.")
        String password
) {
    public UserSignIn toServiceDto() {
        return new UserSignIn(username, password);
    }
}
