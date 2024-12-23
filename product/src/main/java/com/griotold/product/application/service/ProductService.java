package com.griotold.product.application.service;

import com.griotold.exception.ErrorCode;
import com.griotold.exception.LogisticsException;
import com.griotold.product.application.dto.ProductCreate;
import com.griotold.product.application.dto.ProductResponse;
import com.griotold.product.domain.entity.Product;
import com.griotold.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductCreate productCreate) {
        log.info("createProduct.ProductCreate : {}", productCreate);
        Product product = Product.create(
                productCreate.companyId(),
                productCreate.hubId(),
                productCreate.name(),
                productCreate.quantity()
        );
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse getProduct(UUID productId) {
        log.info("getProduct.ProductId : {}", productId);
        return productRepository.findById(productId)
                .map(ProductResponse::from)
                .orElseThrow(() -> new LogisticsException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
