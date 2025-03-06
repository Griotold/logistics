package com.griotold.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum CompanyType {
    MANUFACTURER("생산"),
    RECIPIENT ("수령"),

    ;

    private final String description;

    public static Optional<CompanyType> of(String request) {
        if (request == null) {
            return Optional.empty();
        }

        return switch (request.toUpperCase()) {
            case "MANUFACTURER" -> Optional.of(MANUFACTURER);
            case "RECIPIENT" -> Optional.of(RECIPIENT);
            default -> Optional.empty();
        };
    }
}
