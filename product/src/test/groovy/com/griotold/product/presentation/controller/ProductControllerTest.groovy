package com.griotold.product.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.griotold.common.exception.ErrorCode
import com.griotold.common.exception.GlobalExceptionHandler
import com.griotold.common.exception.LogisticsException
import com.griotold.product.application.dto.ProductResponse
import com.griotold.product.application.service.ProductService
import com.griotold.product.presentation.dto.ProductCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ProductControllerTest extends Specification {
    ProductService productService = Mock(ProductService)
    ProductController productController
    MockMvc mockMvc
    ObjectMapper objectMapper = new ObjectMapper()

    void setup() {
        productController = new ProductController(productService)
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())  // 예외 핸들러 등록
                .build()
    }

    def "createProduct 메서드는 상품을 생성하고 성공 응답을 반환한다"() {
        given: "상품 생성 요청이 주어질 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def request = new ProductCreateRequest(companyId, hubId, "Test Product", 100)

        when: "createProduct API 를 호출하면"
        def response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn().response

        then: "상태 코드는 200 OK"
        response.status == HttpStatus.OK.value()
    }

    def "createProduct 메서드는 companyId가 필수 입력값이다."() {
        given: "companyId가 없는 상품 생성 요청이 주어질 때"
        def companyId = null
        def hubId = UUID.randomUUID()
        def request = new ProductCreateRequest(companyId, hubId, "Test Product", 100)

        when: "createProduct API 를 호출하면"
        def response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn().response

        then: "상태 코드는 400 BAD_REQUEST"
        response.status == HttpStatus.BAD_REQUEST.value()

        and: "에러 메시지는 companyId가 필수 입력값임을 나타낸다"
        def jsonResponse = objectMapper.readValue(response.contentAsString, Map)
        jsonResponse.message == "companyId is a required field."
    }

    def "createProduct 메서드는 hubId가 필수 입력값이다."() {
        given: "hubId가 없는 상품 생성 요청이 주어질 때"
        def companyId = UUID.randomUUID()
        def hubId = null
        def request = new ProductCreateRequest(companyId, hubId, "Test Product", 100)

        when: "createProduct API 를 호출하면"
        def response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn().response

        then: "상태 코드는 400 OK"
        response.status == HttpStatus.BAD_REQUEST.value()

        and: "에러 메시지는 hubId가 필수 입력값임을 나타낸다"
        def jsonResponse = objectMapper.readValue(response.contentAsString, Map)
        jsonResponse.message == "hubId is a required field."
    }

    def "createProduct 메서드는 name 이 필수 입력값이다."() {
        given: "name 이 없는 상품 생성 요청이 주어질 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def request = new ProductCreateRequest(companyId, hubId, null, 100)

        when: "createProduct API 를 호출하면"
        def response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn().response

        then: "상태 코드는 400 OK"
        response.status == HttpStatus.BAD_REQUEST.value()

        and: "에러 메시지는 name 이 필수 입력값을 나타낸다."
        def jsonResponse = objectMapper.readValue(response.contentAsString, Map)
        jsonResponse.message == "name is a required field."
    }

    //    @Unroll("createProduct 메서드는 quantity(#quantity)로 호출되었을 때 에러를 반환한다.")
    def "createProduct 메서드는 quantity 가 양수 입력값이어야 한다."(int quantity) {
        given: "quantity 가 0, 또는 음수 입력을 받을 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def request = new ProductCreateRequest(companyId, hubId, "Test Product", quantity)

        when: "createProduct API 를 호출하면"
        def response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn().response

        then: "상태 코드는 400 OK"
        response.status == HttpStatus.BAD_REQUEST.value()

        and: "에러 메시지는 quantity 가 필수 입력값을 나타낸다."
        def jsonResponse = objectMapper.readValue(response.contentAsString, Map)
        jsonResponse.message == "quantity is a positive value."

        where:
        quantity | _
        0        | _
        -1       | _
    }

    def "getProduct() - 상품 조회 성공"() {
        given: "상품 ID와 예상되는 응답이 주어질 때"
        def productId = UUID.randomUUID()
        def expectedResponse = new ProductResponse(
                productId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Test Product",
                100
        )

        when: "getProduct API 를 호출하면"
        def resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/${productId}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "ProductService 의 getProduct 메서드가 호출되고"
        1 * productService.getProduct(productId) >> expectedResponse

        and: "응답이 올바르다"
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.data.productId').value(expectedResponse.productId().toString()))
                .andExpect(jsonPath('$.data.companyId').value(expectedResponse.companyId().toString()))
                .andExpect(jsonPath('$.data.hubId').value(expectedResponse.hubId().toString()))
                .andExpect(jsonPath('$.data.name').value(expectedResponse.name()))
                .andExpect(jsonPath('$.data.quantity').value(expectedResponse.quantity()))
    }

    def "getProduct() - 없는 상품 조회시 예외가 발생한다."() {
        given: "존재하지 않는 상품 ID가 주어질 때"
        def nonExistentProductId = UUID.randomUUID()
        def expectedException = new LogisticsException(ErrorCode.ENTITY_NOT_FOUND)

        when: "getProduct API를 호출하면"
        def resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/${nonExistentProductId}")
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then: "ProductService의 getProduct 메서드가 호출되고 예외가 발생한다"
        1 * productService.getProduct(nonExistentProductId) >> { throw expectedException }

        and: "응답이 올바르다"
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('$.message').value(ErrorCode.ENTITY_NOT_FOUND.getMessage()))
    }
}
