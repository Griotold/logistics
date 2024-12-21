package com.griotold.product.application.service

import com.griotold.product.application.dto.ProductCreate
import com.griotold.product.domain.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceItTest extends Specification {

    @Autowired
    ProductService productService

    @Autowired
    ProductRepository productRepository

    def "createProduct 메서드는 Product 를 저장하고 ProductResponse 를 반환한다."() {
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
}
