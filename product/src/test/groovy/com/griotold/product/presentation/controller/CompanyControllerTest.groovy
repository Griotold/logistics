package com.griotold.product.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.griotold.product.application.service.CompanyService
import com.griotold.product.presentation.dto.company.CompanyCreateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@WebMvcTest(CompanyController.class)
class CompanyControllerTest extends Specification {

    @Autowired MockMvc mockMvc
    @Autowired ObjectMapper om
    @MockBean CompanyService companyService

    def "createCompany - 성공 "() {
        given:
        def hubId = UUID.randomUUID()
        def request = new CompanyCreateRequest(hubId, "NewCompany", "MANUFACTURER", "123 Test St.")

        expect: "상태 코드 200 OK"
        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)))
                .andExpect(status().isOk())
    }

    @Unroll("#field 유효성 검사 실패")
    def "createCompany - 유효성 검사 실패"() {
        given:
        def request = new CompanyCreateRequest(hubId, name, type, address)

        when:
        def response = mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request)))
                        .andExpect(status().isBadRequest())
                        .andReturn().response

        then: "유효성 검사에 실패한 field 가 에러 메시지로 응답된다. "
                def jsonResponse = om.readValue(response.contentAsString, Map)
                jsonResponse.message.contains(field)
        where:
                field      | hubId             | name         | type          | address
                "hubId"    | null              | "testCompany"| "MANUFACTURER"| "testAddress"
                "name"     | UUID.randomUUID() | ""           | "MANUFACTURER"| "testAddress"
                "type"     | UUID.randomUUID() | "testCompany"| null          | "testAddress"
                "address"  | UUID.randomUUID() | "testCompany"| "MANUFACTURER"| null
    }

}
