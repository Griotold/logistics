package com.griotold.auth.domain.entity

import com.griotold.auth.domain.enums.Role
import spock.lang.Specification

class UserTest extends Specification {

    def "create - 성공"() {
        given: "아래의 입력이 주어졌을 때"
        String username = "testUser"
        String password = "securePassword"
        String email = "test@example.com"
        Role role = Role.HUB

        when: "create 를 호출하면"
        User user = User.create(username, password, email, role)

        then: "User 인스턴스가 생성된다."
        with(user) {
            it.username == username
            it.password == password
            it.email == email
            it.role == role
        }

        and: "userId는 null 이어야 한다"
        user.id == null
    }
}
