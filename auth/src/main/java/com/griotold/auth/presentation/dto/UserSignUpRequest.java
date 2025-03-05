package com.griotold.auth.presentation.dto;

import com.griotold.auth.application.dto.UserCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignUpRequest(
        @Pattern(regexp = "^[a-z]{1,20}$",
                message = "username 은 알파벳 소문자 1 ~ 20 이어야 합니다.")
        String username,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{2,20}$",
                message = "password 는 영어 소문자와 숫자를 각각 1개 이상 포함한 2~20자리여야 합니다.")
        String password,

        @Email(message = "email 형식이어야 합니다.")
        String email,

        @Size(max = 10, message = "role 은 알파벳 10글자 이내이어야 합니다.")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "role 은 알파벳 대소문자만 입력 가능합니다.")
        String role
) {
    public UserCreate toServiceDto() {
        return new UserCreate(username, password, email, role);
    }
}
