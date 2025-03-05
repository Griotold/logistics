package com.griotold.auth.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.griotold.auth.application.service.AuthService
import com.griotold.auth.infra.config.security.AuthConfig
import com.griotold.auth.presentation.dto.UserSignUpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import org.springframework.http.MediaType
import spock.lang.Unroll


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@Import(AuthConfig)
class AuthControllerTest extends Specification {
    @Autowired MockMvc mockMvc
    @Autowired ObjectMapper om
    @MockBean AuthService authService

    def "signUp - 성공 "() {
        given:
        def request = new UserSignUpRequest("testuser", "password1", "test@example.com", "HUB")

        expect:
        mockMvc.perform(post("/api/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)))
                .andExpect(status().isOk())
    }

    @Unroll("#field 유효성 검사 실패")
    def "signUp - 유효성 검사 실패"() {
        given:
        def request = new UserSignUpRequest(username, password, email, role)

        when:
        def response = mockMvc.perform(post("/api/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn().response

        then: "유효성 검사에 실패한 field 가 에러 메시지로 응답된다. "
        def jsonResponse = om.readValue(response.contentAsString, Map)
        jsonResponse.message.contains(field)

        where:
        field      | username   | password   | email             | role
        "username" | "USER1"    | "password1"| "test@example.com"| "USER"
        "password" | "testuser" | "password" | "test@example.com"| "USER"
        "email"    | "testuser" | "password1"| "invalid-email"   | "USER"
        "role"     | "testuser" | "password1"| "test@example.com"| "USER123"
    }
}
