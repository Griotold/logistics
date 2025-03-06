package com.griotold.product.application.service;

import com.griotold.common.exception.ErrorCode;
import com.griotold.common.exception.LogisticsException;
import com.griotold.product.application.dto.company.CompanyCreate;
import com.griotold.product.application.dto.company.CompanyResponse;
import com.griotold.product.domain.entity.Company;
import com.griotold.product.domain.enums.CompanyType;
import com.griotold.product.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public CompanyResponse createCompany(CompanyCreate companyCreate) {
        log.info("createCompany.companyCreate : {}", companyCreate);
        // 권한 검증
        // todo HUB 권한은 요청하는 HUB 의 관리자인지 확인 해야함 -> userId 와 hubId 받아서 HUB 관리자가 맞는 지 검증

        validateName(companyCreate.name());

        CompanyType type = CompanyType.of(companyCreate.type())
                .orElseThrow(() -> new LogisticsException(ErrorCode.INVALID_COMPANY_TYPE));

        Company company = Company.create(companyCreate.hubId(), companyCreate.name(), type, companyCreate.address());

        return CompanyResponse.from(companyRepository.save(company));
    }

    private void validateName(String name) {
        if (companyRepository.existsByName(name)) {
            throw new LogisticsException(ErrorCode.DUPLICATE_COMPANY_NAME);
        }
    }
}
