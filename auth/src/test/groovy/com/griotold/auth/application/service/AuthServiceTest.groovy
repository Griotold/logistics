package com.griotold.auth.application.service

import com.griotold.auth.application.dto.UserSignIn
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

    @Autowired JwtTokenProvider jwtTokenProvider

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

    def "signIn - 성공해서 String token을 반환하는 경우"() {
        given: "유효한 사용자 정보가 주어졌을 때"
        def username = "validUser"
        def password = "validPassword"
        def encodedPassword = passwordEncoder.encode(password)
        def user = User.create(username, encodedPassword, "user@example.com", Role.HUB)
        userRepository.save(user)

        when: "signIn 메서드를 호출하면"
        def token = authService.singIn(new UserSignIn(username, password))

        then: "JWT 토큰이 반환된다."
        token != null
    }

    def "signIn - 없는 User라 예외 발생하는 경우"() {
        given: "존재하지 않는 username이 주어졌을 때"
        def username = "nonExistentUser"
        def password = "password"

        when: "signIn 메서드를 호출하면"
        authService.singIn(new UserSignIn(username, password))

        then: "ENTITY_NOT_FOUND 예외가 발생한다."
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.NOT_FOUND
            message == ErrorCode.ENTITY_NOT_FOUND.getMessage()
        }
    }

    def "signIn - password 통과 못해서 예외 발생하는 경우"() {
        given: "유효한 username과 잘못된 password가 주어졌을 때"
        def username = "validUser"
        def correctPassword = "correctPassword"
        def wrongPassword = "wrongPassword"
        def encodedPassword = passwordEncoder.encode(correctPassword)
        def user = User.create(username, encodedPassword, "user@example.com", Role.HUB)
        userRepository.save(user)

        when: "signIn 메서드를 호출하면"
        authService.singIn(new UserSignIn(username, wrongPassword))

        then: "INVALID_PASSWORD 예외가 발생한다."
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.UNAUTHORIZED
            message == ErrorCode.INVALID_PASSWORD.getMessage()
        }
    }

    def "verifyUser - 존재하는 사용자 ID로 호출 시 true 반환"() {
        given: "데이터베이스에 사용자가 존재할 때"
        def user = User.create("existingUser", "password", "user@example.com", Role.HUB)
        def savedUser = userRepository.save(user)

        when: "verifyUser 메서드를 호출하면"
        def result = authService.verifyUser(savedUser.getId())

        then: "true가 반환된다"
        result == true
    }

    def "verifyUser - 존재하지 않는 사용자 ID로 호출 시 false 반환"() {
        given: "데이터베이스에 존재하지 않는 사용자 ID"
        def nonExistentUserId = 9999L

        when: "verifyUser 메서드를 호출하면"
        def result = authService.verifyUser(nonExistentUserId)

        then: "false가 반환된다"
        result == false
    }

}
