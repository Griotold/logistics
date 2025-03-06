package com.griotold.product.domain.repository

import com.griotold.product.domain.entity.Company
import com.griotold.product.domain.enums.CompanyType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CompanyRepositoryTest extends Specification {

    @Autowired
    CompanyRepository companyRepository

    def "existsByName - 존재하는 경우 true 반환"() {
        given: "'testCompany' 라는 이름의 업체가 존재할 때"
        def name = "testCompany"
        def hubId = UUID.randomUUID()
        def address = "testAddress"
        def user = Company.create(hubId, name, CompanyType.MANUFACTURER, address)
        companyRepository.save(user)

        when: "existsByUsername 을 호출하면"
        def result = companyRepository.existsByName(name)

        then: "true 를 리턴한다."
        result == true
    }

    def "existsByName - 존재하지 않는 경우 false 반환"() {
        given: "존재하지 않는 업체 이름"
        def nonExistentName = "nonExistentCompany"

        when: "existsByName 을 호출하면"
        def result = companyRepository.existsByName(nonExistentName)

        then: "false 를 리턴한다."
        result == false
    }
}
