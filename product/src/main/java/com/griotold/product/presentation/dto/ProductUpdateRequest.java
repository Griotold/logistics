package com.griotold.product.presentation.dto;

import com.griotold.product.application.dto.ProductUpdate;

import java.util.UUID;

public record ProductUpdateRequest(
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {
    public ProductUpdate toServiceDto() {
        return new ProductUpdate(companyId, hubId, name, quantity);
    }
}
