package com.griotold.product.application.service

import com.griotold.common.exception.ErrorCode
import com.griotold.common.exception.LogisticsException
import com.griotold.product.application.dto.ProductCreate
import com.griotold.product.application.dto.ProductUpdate
import com.griotold.product.domain.entity.Product
import com.griotold.product.domain.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
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


    def "createProduct 는 Product 를 저장하고 ProductResponse 를 반환한다."() {
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

    def "getProduct - 상품 조회 성공"() {
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

    def "getProduct - 없는 상품 조회 시 예외가 발생한다."() {
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

    def "updateProduct 는 기존 Product 를 수정하고 수정된 ProductResponse 를 반환한다."() {
        given: "기존에 저장된 Product가 있을 때"
        def companyId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        def hubId = UUID.fromString("22222222-2222-2222-2222-222222222222")
        def product = Product.create(companyId, hubId, "Old Product", 5)
        def savedProduct = productRepository.save(product)

        and: "수정할 Product 정보가 주어졌을 때"
        def newCompanyId = UUID.fromString("33333333-3333-3333-3333-333333333333")
        def newHubId = UUID.fromString("44444444-4444-4444-4444-444444444444")
        def productUpdate = new ProductUpdate(newCompanyId, newHubId, "Updated Product", 20)

        when: "updateProduct()를 호출하면"
        def updatedResponse = productService.updateProduct(savedProduct.getId(), productUpdate)

        then: "수정된 ProductResponse가 반환되고"
        with(updatedResponse) {
            it != null
            it.companyId() == newCompanyId
            it.hubId() == newHubId
            it.name() == "Updated Product"
            it.quantity() == 20
        }

        and: "데이터베이스에서도 수정된 내용이 반영된다."
        def updatedProduct = productRepository.findById(savedProduct.getId()).orElse(null)
        with(updatedProduct) {
            it != null
            it.companyId == newCompanyId
            it.hubId == newHubId
            it.name == "Updated Product"
            it.quantity == 20
        }
    }

    def "updateProduct - 없는 상품을 수정하려고 하면 예외가 발생한다."() {
        given: "존재하지 않는 상품 ID와 수정 요청이 주어질 때"
        def nonExistentProductId = UUID.randomUUID()
        def productUpdate = new ProductUpdate(UUID.randomUUID(), UUID.randomUUID(), "Non-existent Update", 10)

        when: "updateProduct()를 호출하면"
        productService.updateProduct(nonExistentProductId, productUpdate)

        then: "LogisticsException이 발생한다"
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.NOT_FOUND
            message == ErrorCode.ENTITY_NOT_FOUND.getMessage()
        }
    }

    def "deleteProduct - 상품 삭제 성공"() {
        given: "기존에 저장된 product 가 있을 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def product = Product.create(companyId, hubId, "Test Product", 5)
        productRepository.save(product)

        when: "deleteProduct() 를 호출하면"
        productService.deleteProduct(product.getId())

        then: "isDeleted = true 를 리턴한다."
        def deletedProduct = productRepository.findById(product.getId()).orElse(null)
        with(deletedProduct) {
            it != null
            it.isDeleted() == true
        }
    }

    def "deleteProduct - 없는 상품을 삭제하려고 하면 예외가 발생한다."() {
        given: "존재하지 않는 ID로 삭제 요청이 들어올 때"
        def nonExistentProductId = UUID.randomUUID()

        when: "deleteProduct() 를 호출하면"
        productService.deleteProduct(nonExistentProductId)

        then: "LogisticsException 이 발생한다"
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.NOT_FOUND
            message == ErrorCode.ENTITY_NOT_FOUND.getMessage()
        }
    }

    def "deleteProduct - 이미 삭제된 상품은 예외가 발생한다."() {
        given: "기존에 저장된 product 가 있을 때"
        def companyId = UUID.randomUUID()
        def hubId = UUID.randomUUID()
        def product = Product.create(companyId, hubId, "Test Product", 5)
        productRepository.save(product)

        and: "그리고, 이미 삭제가 되었을 때"
        productService.deleteProduct(product.getId())

        when: "deleteProduct() 를 호출하면"
        productService.deleteProduct(product.getId())

        then: "LogisticsException 이 발생한다"
        def exception = thrown(LogisticsException)
        with(exception) {
            httpStatus == HttpStatus.CONFLICT
            message == ErrorCode.ALREADY_DELETED.getMessage()
        }
    }
}
