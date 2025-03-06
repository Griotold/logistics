package com.griotold.product.domain.enums

import spock.lang.Specification
import spock.lang.Unroll

class CompanyTypeTest extends Specification {

    @Unroll("입력값 #input 일 때, 예상 결과는 Optional 로 감싼 #expectedType 이어야 한다")
    def "of 메서드가 대소문자 상관없이 올바른 CompanyType을 반환해야 한다"() {
        expect:
        CompanyType.of(input) == Optional.of(expectedType)

        where:
        input          || expectedType
        "MANUFACTURER" || CompanyType.MANUFACTURER
        "manufacturer" || CompanyType.MANUFACTURER
        "Manufacturer" || CompanyType.MANUFACTURER
        "RECIPIENT"    || CompanyType.RECIPIENT
        "recipient"    || CompanyType.RECIPIENT
        "Recipient"    || CompanyType.RECIPIENT
    }

    @Unroll("입력값 #input 일 때, 예상 결과는 Optional.empty() 이어야 한다")
    def "of 메서드가 유효하지 않은 입력값에 대해 Optional.empty()를 반환해야 한다"() {
        expect:
        CompanyType.of(input) == Optional.empty()

        where:
        input << [null, "", "INVALID", "123", "PRODUCER", "CONSUMER"]
    }
}

