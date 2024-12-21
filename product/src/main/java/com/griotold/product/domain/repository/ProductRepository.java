package com.griotold.product.domain.repository;

import com.griotold.product.domain.entity.Product;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    Product save(Product product);
}
