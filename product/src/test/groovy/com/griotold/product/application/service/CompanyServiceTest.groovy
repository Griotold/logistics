package com.griotold.product.application.service

import com.griotold.common.exception.ErrorCode
import com.griotold.common.exception.LogisticsException
import com.griotold.product.application.dto.company.CompanyCreate
import com.griotold.product.application.dto.company.CompanyResponse
import com.griotold.product.domain.entity.Company
import com.griotold.product.domain.enums.CompanyType
import com.griotold.product.domain.repository.CompanyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CompanyServiceTest extends Specification {

    @Autowired CompanyService companyService
    @Autowired CompanyRepository companyRepository

    def "createCompany - 회사 생성 성공 테스트"() {
        given: "회사 정보가 주어졌을 때"
        def hubId = UUID.randomUUID()
        def companyCreate = new CompanyCreate(hubId, "NewCompany", "MANUFACTURER", "123 Test St.")

        when: "createCompany를 호출하면"
        def response = companyService.createCompany(companyCreate)

        then: "CompanyResponse가 반환된다."
        response instanceof CompanyResponse
        with(response) {
            name == "NewCompany"
            type == CompanyType.MANUFACTURER.name()
            address == "123 Test St."
        }

        and: "데이터베이스에도 저장된다."
        def savedCompany = companyRepository.findById(response.id).orElse(null)
        with(savedCompany) {
            this != null
            name == "NewCompany"
            type == CompanyType.MANUFACTURER
            address == "123 Test St."
        }
    }

    def "createCompany - 중복된 회사명으로 생성 시 예외 발생"() {
        given: "중복된 회사명이 데이터베이스에 저장되어 있을 때"
        def hubId = UUID.randomUUID()
        def existingCompany = Company.create(hubId, "ExistingCompany", CompanyType.MANUFACTURER, "Existing Address")
        companyRepository.save(existingCompany)

        def companyCreate = new CompanyCreate(hubId, "ExistingCompany", "MANUFACTURER", "New Address")

        when: "createCompany를 호출하면"
        companyService.createCompany(companyCreate)

        then: "예외가 발생한다."
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.CONFLICT
            message == ErrorCode.DUPLICATE_COMPANY_NAME.getMessage()
        }
    }

    def "createCompany - 잘못된 CompanyType 입력 시 예외 발생"() {
        given: "잘못된 CompanyType을 입력할 때"
        def hubId = UUID.randomUUID()
        def companyCreate = new CompanyCreate(hubId, "NewCompany", "INVALID_TYPE", "123 Test St.")

        when: "createCompany를 호출하면"
        companyService.createCompany(companyCreate)

        then: "예외가 발생한다."
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.BAD_REQUEST
            message == ErrorCode.INVALID_COMPANY_TYPE.getMessage()
        }
    }
}

