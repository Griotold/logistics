package com.griotold.product.presentation.dto;

import com.griotold.product.application.dto.ProductCreate;

import java.util.UUID;

public record ProductCreateRequest(
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {

    public ProductCreate toServiceDto() {
        return new ProductCreate(companyId, hubId, name, quantity);
    }
}
