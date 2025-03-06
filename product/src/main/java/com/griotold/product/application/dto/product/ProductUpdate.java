package com.griotold.product.application.dto.product;

import java.util.UUID;

public record ProductUpdate(
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {
}
