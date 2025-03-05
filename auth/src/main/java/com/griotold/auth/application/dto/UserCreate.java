package com.griotold.auth.application.dto;

public record UserCreate(
        String username,
        String password,
        String email,
        String role
) {
}
