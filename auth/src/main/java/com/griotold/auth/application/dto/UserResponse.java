package com.griotold.auth.application.dto;

import com.griotold.auth.domain.entity.User;

public record UserResponse(
        Long userId,
        String username,
        String email,
        String roll) {

    public static UserResponse from(final User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString()
        );
    }
}
