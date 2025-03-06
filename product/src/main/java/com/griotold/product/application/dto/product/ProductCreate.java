package com.griotold.product.application.dto.product;

import java.util.UUID;

public record ProductCreate(
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {
}
