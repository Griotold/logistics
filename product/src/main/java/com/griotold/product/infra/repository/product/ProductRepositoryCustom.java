package com.griotold.product.infra.repository.product;

import com.griotold.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepositoryCustom {

    Page<Product> search(UUID companyId, String name, Pageable pageable);
}
