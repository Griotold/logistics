package com.griotold.auth.application.dto;

public record UserSignIn(
        String username,
        String password
) {
}
