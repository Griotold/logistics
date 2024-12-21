package com.griotold.product.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.griotold.product.application.dto.ProductResponse
import com.griotold.product.application.service.ProductService
import com.griotold.product.presentation.dto.ProductCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class ProductControllerTest extends Specification {
    ProductService productService = Mock(ProductService)
    ProductController productController
    MockMvc mockMvc
    ObjectMapper objectMapper = new ObjectMapper()

    void setup() {
        productController = new ProductController(productService)
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build()
    }

    def "createProduct 메서드는 상품을 생성하고 성공 응답을 반환한다"() {
        given: "상품 생성 요청이 주어질 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def request = new ProductCreateRequest(companyId, hubId, "Test Product", 100)
        def expectedResponse = new ProductResponse(UUID.randomUUID(), companyId, hubId, "Test Product", 100)

        when: "createProduct API를 호출하면"
        def response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn().response

        then: "상태 코드는 200 OK"
        response.status == HttpStatus.OK.value()

        and: "productService의 createProduct 메서드가 한 번 호출되고"
        1 * productService.createProduct(_) >> { args ->
            def dto = args[0]
            assert dto.companyId() == companyId
            assert dto.hubId() == hubId
            assert dto.name() == "Test Product"
            assert dto.quantity() == 100
            return expectedResponse
        }
    }
}
