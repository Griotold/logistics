package com.griotold.product.domain.repository;

import com.griotold.product.domain.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

    Optional<Company> findById(UUID id);

    Company save(Company company);

    boolean existsByName(String name);
}
