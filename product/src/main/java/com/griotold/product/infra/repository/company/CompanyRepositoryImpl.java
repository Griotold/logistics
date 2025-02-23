package com.griotold.product.infra.repository.company;

import com.griotold.product.domain.entity.Company;
import com.griotold.product.domain.repository.CompanyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepositoryImpl extends JpaRepository<Company, UUID>, CompanyRepository {
}
