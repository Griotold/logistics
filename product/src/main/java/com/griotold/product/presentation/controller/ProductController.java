package com.griotold.product.presentation.controller;

import com.griotold.common.presentation.ApiResponse;
import com.griotold.product.application.dto.ProductResponse;
import com.griotold.product.application.service.ProductService;
import com.griotold.product.presentation.dto.ProductCreateRequest;
import com.griotold.product.presentation.dto.ProductUpdateRequest;
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

    /**
     * 상품 생성
     * */
    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        ProductResponse response = productService.createProduct(productCreateRequest.toServiceDto());
        return ApiResponse.success(response);
    }

    /**
     * 상품 단건 조회
     * */
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable("id") UUID id) {
        ProductResponse response = productService.getProduct(id);
        return ApiResponse.success(response);
    }

    // 페이징

    /**
     * 상품 수정
     * */
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable("id") UUID id,
                                                      @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        ProductResponse response = productService.updateProduct(id, productUpdateRequest.toServiceDto());
        return ApiResponse.success(response);
    }

    // 삭제

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProduct(id);
        return ApiResponse.success();
    }
}
