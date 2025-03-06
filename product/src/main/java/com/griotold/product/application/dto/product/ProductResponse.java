package com.griotold.product.application.dto.product;

import com.griotold.product.domain.entity.Product;

import java.util.UUID;

public record ProductResponse(
        UUID productId,
        UUID companyId,
        UUID hubId,
        String name,
        Integer quantity
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCompanyId(),
                product.getHubId(),
                product.getName(),
                product.getQuantity()
        );
    }
}
