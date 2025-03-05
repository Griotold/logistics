package com.griotold.auth.application.service

import com.griotold.common.exception.ErrorCode
import com.griotold.common.exception.LogisticsException
import com.griotold.auth.domain.entity.User
import com.griotold.auth.domain.enums.Role
import com.griotold.auth.application.dto.UserCreate
import com.griotold.auth.application.dto.UserResponse
import com.griotold.auth.domain.repository.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest extends Specification {

    @Autowired AuthService authService

    @Autowired UserRepository userRepository

    @Autowired PasswordEncoder passwordEncoder

    @Autowired EntityManager em

    def "singUp - 회원가입 성공 테스트"() {
        given: "회원 정보가 주어졌을 때"
        def userCreate = new UserCreate("newUser", "password", "user@example.com", "HUB")

        when: "signUp 을 호출하면"
        def response = authService.singUp(userCreate)

        then: "UserResponse 가 반환된다."
        response instanceof UserResponse
        with(response) {
            username == "newUser"
            email == "user@example.com"
        }

        em.flush()
        em.clear()

        and: "데이터베이스에도 저장된다."
        def savedUser = userRepository.findById(response.userId()).orElse(null)
        with(savedUser) {
            this != null
            username == "newUser"
            email == "user@example.com"
        }
        savedUser != null
        savedUser.username == "newUser"
        savedUser.email == "user@example.com"
    }

    def "signUp - 중복된 username으로 회원가입 시 예외 발생"() {
        given: "중복된 username 이 데이터베이스에 저장되어 있을 때"
        def userCreate = new UserCreate("existingUser", "password", "user@example.com", "HUB")
        userRepository.save(User.create("existingUser", "password", "user@example.com", Role.HUB))

        when: "signUp 을 호출하면"
        authService.singUp(userCreate)

        then: "예외가 발생한다."
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.CONFLICT
            message == ErrorCode.DUPLICATE_USERNAME.getMessage()
        }
    }

    def "signUp - 없는 Role 입력 시 예외 발생"() {
        given: "없는 Role 입력할 때"
        def userCreate = new UserCreate("newUser", "password", "user@example.com", "INVALID_ROLE")

        when: "signUp 을 호출하면"
        authService.singUp(userCreate)

        then: "예외가 발새한다."
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.BAD_REQUEST
            message == ErrorCode.INVALID_ROLE.getMessage()
        }
    }
}
