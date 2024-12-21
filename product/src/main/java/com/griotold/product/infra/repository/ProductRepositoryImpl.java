package com.griotold.product.infra.repository;

import com.griotold.product.domain.entity.Product;
import com.griotold.product.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepositoryImpl extends JpaRepository<Product, UUID>, ProductRepository {
}
