package com.griotold.product.presentation.dto.company;

import com.griotold.product.application.dto.company.CompanyCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompanyCreateRequest(
        @NotNull(message = "hubId is a required field.")
        UUID hubId,
        @NotBlank(message = "name is a required field.")
        String name,
        @NotBlank(message = "type is a required field.")
        String type,
        @NotBlank(message = "address is a required field.")
        String address
) {
    public CompanyCreate toServiceDto() {
        return new CompanyCreate(hubId, name, type, address);
    }
}
