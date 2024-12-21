package com.griotold.product.presentation.controller;

import com.griotold.presentation.ApiResponse;
import com.griotold.product.application.dto.ProductResponse;
import com.griotold.product.application.service.ProductService;
import com.griotold.product.presentation.dto.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        log.info("createProduct.ProductCreateRequest {}", productCreateRequest);
        ProductResponse response = productService.createProduct(productCreateRequest.toServiceDto());
        return ApiResponse.success(response);
    }
}
