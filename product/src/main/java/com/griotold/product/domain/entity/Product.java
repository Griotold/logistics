package com.griotold.product.domain.entity;

import com.griotold.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public static Product create(UUID companyId, UUID hubId, String name, Integer quantity) {
        return Product.builder()
                .companyId(companyId)
                .hubId(hubId)
                .name(name)
                .quantity(quantity)
                .build();
    }

    public void update(UUID companyId, UUID hubId, String name, Integer quantity) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.name = name;
        this.quantity = quantity;
    }
}