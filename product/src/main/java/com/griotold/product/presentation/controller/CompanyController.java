package com.griotold.product.presentation.controller;

import com.griotold.common.presentation.ApiResponse;
import com.griotold.product.application.dto.company.CompanyResponse;
import com.griotold.product.application.service.CompanyService;
import com.griotold.product.infra.aspect.RequireRole;
import com.griotold.product.presentation.dto.company.CompanyCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/companies")
@RestController
public class CompanyController {

    private final CompanyService companyService;

    /**
     * 업체 생성
     * */
    @RequireRole(value = {"MASTER", "HUB"})
    @PostMapping
    public ApiResponse<CompanyResponse> createCompany(@Valid @RequestBody CompanyCreateRequest companyCreateRequest,
                                                      @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                      @RequestHeader(value = "X-Role", required = true) String role) {
        log.info("userId: {}, role: {}", userId, role);
        CompanyResponse response = companyService.createCompany(companyCreateRequest.toServiceDto());
        return ApiResponse.success(response);
    }
}
