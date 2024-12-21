package com.griotold.product.domain.repository;

import com.griotold.product.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> findById(UUID id);

    Product save(Product product);
}
