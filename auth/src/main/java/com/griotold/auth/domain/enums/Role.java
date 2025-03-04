package com.griotold.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum Role {
    MASTER("ROLE_MASTER"),
    HUB("ROLE_HUB"),
    DELIVERY("ROLE_DELIVERY"),
    COMPANY("ROLE_COMPANY");

    private final String role;

    public static Optional<Role> of(String request) {
        if (request == null) {
            return Optional.empty();
        }

        return switch (request.toUpperCase()) {
            case "MASTER" -> Optional.of(MASTER);
            case "HUB" -> Optional.of(HUB);
            case "DELIVERY" -> Optional.of(DELIVERY);
            case "COMPANY" -> Optional.of(COMPANY);
            default -> Optional.empty();
        };
    }

}
