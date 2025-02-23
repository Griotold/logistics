package com.griotold.product.domain.repository;

import com.griotold.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> findById(UUID id);

    Product save(Product product);

    <S extends Product> List<S> saveAll(Iterable<S> entities); // saveAll 메서드 선언

    Page<Product> search(UUID companyId, String name, Pageable pageable);

}
