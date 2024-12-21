package com.griotold.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyType {
    MANUFACTURER("생산"),
    RECIPIENT ("수령"),

    ;

    private final String description;
}
