package com.griotold.product.application.service

import com.griotold.common.exception.ErrorCode
import com.griotold.common.exception.LogisticsException
import com.griotold.product.domain.entity.Product
import com.griotold.product.application.dto.ProductCreate
import com.griotold.product.domain.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceItTest extends Specification {

    @Autowired
    ProductService productService

    @Autowired
    ProductRepository productRepository

    def "createProduct() 는 Product 를 저장하고 ProductResponse 를 반환한다."() {
        given: "새로은 Product 생성 요청이 들어올 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID();
        def productCreate = new ProductCreate(companyId, hubId, "Test Product", 10)

        when: "createProduct()를 호출하면"
        def productResponse = productService.createProduct(productCreate)

        then: "ProductResponse가 생성되고"
        productResponse != null
        productResponse.productId() != null
        productResponse.name() == "Test Product"
        productResponse.quantity() == 10

        and: "Product 는 데이터베이스에서 조회할 수 있다."
        def savedProduct = productRepository.findById(productResponse.productId()).orElse(null)
        savedProduct != null
        savedProduct.getName() == "Test Product"
        savedProduct.getQuantity() == 10
        savedProduct.getCompanyId() == companyId
        savedProduct.getHubId() == hubId
    }

    def "getProduct() - 상품 조회 성공"() {
        given: "상품이 데이터베이스에 저장되어 있을 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def product = Product.create(companyId, hubId, "Test Product", 10)
        def savedProduct = productRepository.save(product)

        when: "getProduct()를 호출하면"
        def productResponse = productService.getProduct(savedProduct.getId())

        then: "저장된 상품 정보와 일치하는 ProductResponse가 반환된다"
        with(productResponse) {
            it.productId() == savedProduct.getId()
            it.companyId() == companyId
            it.hubId() == hubId
            it.name() == "Test Product"
            it.quantity() == 10
        }
    }

    def "getProduct() - 없는 상품 조회 시 예외가 발생한다."() {
        given: "존재하지 않는 상품 ID가 주어질 때"
        def nonExistentProductId = UUID.randomUUID()

        when: "getProduct()를 호출하면"
        productService.getProduct(nonExistentProductId)

        then: "LogisticsException이 발생한다"
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.NOT_FOUND
            message == ErrorCode.ENTITY_NOT_FOUND.getMessage()
        }
    }
}
