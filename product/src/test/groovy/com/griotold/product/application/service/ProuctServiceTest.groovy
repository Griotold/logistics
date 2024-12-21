package com.griotold.product.application.service

import com.griotold.product.domain.entity.Product
import com.griotold.product.application.dto.ProductCreate
import com.griotold.product.domain.repository.ProductRepository
import org.springframework.util.ReflectionUtils
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

    private void setPrivateField(Object object, String fieldName, Object fieldValue) {
        def field = ReflectionUtils.findField(object.class, fieldName)
        ReflectionUtils.makeAccessible(field)
        ReflectionUtils.setField(field, object, fieldValue)
    }
}
