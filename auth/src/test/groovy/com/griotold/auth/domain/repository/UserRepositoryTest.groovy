package com.griotold.auth.domain.repository

import com.griotold.auth.domain.entity.User
import com.griotold.auth.domain.enums.Role

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class UserRepositoryTest extends Specification {

    @Autowired
    UserRepository userRepository

    def "existsByUsername - 존재하는 경우 true 반환"() {
        given: "'testUser' 라는 이름의 유저가 존재할 때"
        def username = "testUser"
        def user = User.create(username, "password123", "test@example.com", Role.HUB)
        userRepository.save(user)

        when: "existsByUsername 을 호출하면"
        def result = userRepository.existsByUsername(username)

        then: "true 를 리턴한다."
        result == true
    }

    def "existsByUsername - 존재하지 않는 경우 false 반환"() {
        given: "데이터베이스에 없는 username 의 경우"
        def username = "nonExistentUser"

        when: "existsByUsername 을 호출하면"
        def result = userRepository.existsByUsername(username)

        then: "false 를 리턴한다."
        result == false
    }
}
