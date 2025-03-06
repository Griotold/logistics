package com.griotold.product.application.service

import com.griotold.common.exception.ErrorCode
import com.griotold.common.exception.LogisticsException
import com.griotold.product.domain.entity.Product
import com.griotold.product.application.dto.product.ProductCreate
import com.griotold.product.domain.repository.ProductRepository
import org.springframework.util.ReflectionUtils
import org.springframework.http.HttpStatus
import spock.lang.Specification

class ProductServiceTest extends Specification {

    ProductRepository productRepository
    ProductService productService

    def setup() {
        productRepository = Mock(ProductRepository)
        productService = new ProductService(productRepository)
    }

    def "createProduct 메서드는 Product 를 저장하고 ProductResponse 를 반환한다."() {
        given: "새로운 Product 생성 요청이 들어올 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def productCreate = new ProductCreate(companyId, hubId, "Test Product", 10)
        def savedProduct = Product.create(companyId, hubId, "Test Product", 10)

        // ReflectionUtils를 사용하여 id 설정
        setPrivateField(savedProduct, "id", UUID.randomUUID())

        when: "createProduct()를 호출하면"
        def productResponse = productService.createProduct(productCreate)

        then: "ProductRepository의 save 메서드가 호출되고"
        1 * productRepository.save(_) >> savedProduct

        and: "ProductResponse가 올바르게 생성된다"
        productResponse != null
        productResponse.productId() == savedProduct.getId()
        productResponse.name() == "Test Product"
        productResponse.quantity() == 10
        productResponse.companyId() == companyId
        productResponse.hubId() == hubId
    }

    def "상품 조회 성공"() {
        given: "상품 ID와 상품 엔티티가 주어질 때"
        def productId = UUID.randomUUID()
        def product = Product.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "테스트 상품",
                100
        )

        // ReflectionUtils를 사용하여 id 설정
        setPrivateField(product, "id", productId)

        when: "getProduct()를 호출하면"
        def response = productService.getProduct(productId)

        then: "ProductRepository 의 findById 메서드가 호출되고"
        1 * productRepository.findById(productId) >> Optional.of(product)

        and: "ProductResponse 가 올바르게 생성된다"
        with(response) {
            it.productId() == productId
            it.companyId() == product.getCompanyId()
            it.hubId() == product.getHubId()
            it.name() == product.getName()
            it.quantity() == product.getQuantity()
        }
    }

    def "존재하지 않는 상품 조회시 예외가 발생한다."() {
        given: "존재하지 않는 상품의 id는"
        def productId = UUID.randomUUID()

        when: "getProduct() 호출 시"
        productService.getProduct(productId)

        then: "productRepository.findById() 가 Optional.empty() 를 리턴받고"
        1 * productRepository.findById(productId) >> Optional.empty()

        and: "LogisticsException 이 발생한다."
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.NOT_FOUND
            message == ErrorCode.ENTITY_NOT_FOUND.getMessage()
        }
    }

    private void setPrivateField(Object object, String fieldName, Object fieldValue) {
        def field = ReflectionUtils.findField(object.class, fieldName)
        ReflectionUtils.makeAccessible(field)
        ReflectionUtils.setField(field, object, fieldValue)
    }
}
