package com.griotold.product.application.service;

import com.griotold.common.exception.ErrorCode;
import com.griotold.common.exception.LogisticsException;
import com.griotold.product.application.dto.product.ProductCreate;
import com.griotold.product.application.dto.product.ProductResponse;
import com.griotold.product.application.dto.product.ProductUpdate;
import com.griotold.product.domain.entity.Product;
import com.griotold.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
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

    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductUpdate productUpdate) {
        log.info("updateProduct.ProductUpdate : {}", productUpdate);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new LogisticsException(ErrorCode.ENTITY_NOT_FOUND));
        product.update(
                productUpdate.companyId(),
                productUpdate.hubId(),
                productUpdate.name(),
                productUpdate.quantity()
        );

        return ProductResponse.from(product);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        log.info("deleteProduct.ProductId : {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new LogisticsException(ErrorCode.ENTITY_NOT_FOUND));
        if (product.isDeleted()) {
            throw new LogisticsException(ErrorCode.ALREADY_DELETED);
        }

        // todo userId를 넣는 로직 필요
        product.deleteBase(1L);
    }
}
