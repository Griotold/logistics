package com.griotold.product.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.griotold.product.presentation.dto.product.ProductCreateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerItTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "상품 생성 요청시 성공한다"() {
        given: "상품 생성 요청이 들어왔을 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def request = new ProductCreateRequest(companyId, hubId, "Test Product", 100)

        when: "상품 생성 API 를 호출하면"
        def result = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then: "200 OK"
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.message').value("API 요청에 성공했습니다"))
                .andExpect(jsonPath('$.data.productId').exists())
                .andExpect(jsonPath('$.data.name').value("Test Product"))
                .andExpect(jsonPath('$.data.quantity').value(100))
                .andExpect(jsonPath('$.data.companyId').value(companyId.toString()))
                .andExpect(jsonPath('$.data.hubId').value(hubId.toString()))
    }

}
