package com.griotold.auth.domain.enums

import spock.lang.Specification
import spock.lang.Unroll

class RoleTest extends Specification {
    @Unroll("입력값 #input 일 때, 예상 결과는 Optional 로 감싼 #expectedRole 이어야 한다")
    def "of 메서드가 대소문자 상관없이 올바른 Role을 반환해야 한다"() {
        expect:
        Role.of(input) == Optional.of(expectedRole)

        where:
        input      || expectedRole
        "MASTER"   || Role.MASTER
        "master"   || Role.MASTER
        "MaStEr"   || Role.MASTER
        "HUB"      || Role.HUB
        "hub"      || Role.HUB
        "Hub"      || Role.HUB
        "DELIVERY" || Role.DELIVERY
        "delivery" || Role.DELIVERY
        "COMPANY"  || Role.COMPANY
        "company"  || Role.COMPANY
    }
    @Unroll("입력값 #input 일 때, 예상 결과는 Optional.empty() 이어야 한다")
    def "of 메서드가 유효하지 않은 입력값에 대해 null을 반환해야 한다"() {
        expect:
        Role.of(input) == Optional.empty()

        where:
        input << [null, "", "INVALID", "123", "ROLE_MASTER"]
    }
}
