package com.griotold.product.domain.repository

import com.griotold.product.domain.entity.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductRepositoryTest extends Specification {

    @Autowired
    ProductRepository productRepository

    // todo 테스트 코드 좀 더 작성 필요
    def "search - "() {
        given: "Search criteria and pageable information"
        def products = createDummyProducts()
        def companyId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174004")
        productRepository.saveAll(products)
        Pageable pageable = PageRequest.of(0, 10)

        when: "search method is called"
        Page<Product> result = productRepository.search(companyId1, null, pageable)

        then: "The result should not be null and contain expected data"
        with(result) {
            it != null
            it.content.size() == 5
            it.content.every { it.companyId == companyId1 }
        }
    }

    def createDummyProducts() {
        List<Product> products = new ArrayList<>();
        def companyId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174004")
        def companyId2 = UUID.fromString("456e4567-e89b-12d3-a456-426614174004")
        def hubId = UUID.fromString("789e4567-e89b-12d3-a456-426614174004")
        for (int i = 1; i <= 10; i++) {
            Product product = null;
            if (i % 2 == 1) {
                product = Product.create(companyId1, hubId, "product" + i, 10)
            } else {
                product = Product.create(companyId2, hubId, "product" + i, 20)
            }
            products.add(product)
        }
        return products
    }
}
