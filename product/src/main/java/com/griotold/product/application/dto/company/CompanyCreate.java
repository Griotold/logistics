package com.griotold.product.application.dto.company;

import java.util.UUID;

public record CompanyCreate(
        UUID hubId,
        String name,
        String type,
        String address
) {
}
