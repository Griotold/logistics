package com.griotold.product.presentation.dto;

import com.griotold.product.application.dto.product.ProductCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ProductCreateRequest(
        @NotNull(message = "companyId is a required field.") UUID companyId,
        @NotNull(message = "hubId is a required field.") UUID hubId,
        @NotBlank(message = "name is a required field.") String name,
        @Positive(message = "quantity is a positive value.") Integer quantity
) {

    public ProductCreate toServiceDto() {
        return new ProductCreate(companyId, hubId, name, quantity);
    }
}
