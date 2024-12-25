package com.griotold.product.presentation.controller;

import com.griotold.common.presentation.ApiResponse;
import com.griotold.product.application.dto.ProductResponse;
import com.griotold.product.application.service.ProductService;
import com.griotold.product.presentation.dto.ProductCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        ProductResponse response = productService.createProduct(productCreateRequest.toServiceDto());
        return ApiResponse.success(response);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable("id") UUID id) {
        ProductResponse response = productService.getProduct(id);
        return ApiResponse.success(response);
    }


    // 페이징

    // 수정

    // 삭제
}
