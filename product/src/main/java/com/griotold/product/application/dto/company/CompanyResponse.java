package com.griotold.product.application.dto.company;

import com.griotold.product.domain.entity.Company;
import com.griotold.product.domain.enums.CompanyType;

import java.util.UUID;

public record CompanyResponse(
        UUID id,
        UUID hubId,
        String name,
        String address,
        String type
) {
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getHubId(),
                company.getName(),
                company.getAddress(),
                company.getType().name()
        );
    }
}
