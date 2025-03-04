package com.griotold.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    MASTER("ROLE_MASTER"),
    HUB("ROLE_HUB"),
    DELIVERY("ROLE_DELIVERY"),
    COMPANY("ROLE_COMPANY");

    private final String role;
}
