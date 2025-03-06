package com.griotold.product.domain.entity;

import com.griotold.common.domain.entity.BaseEntity;
import com.griotold.product.domain.enums.CompanyType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_company")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CompanyType type;

    @Column(name = "address")
    private String address;

    public static Company create(UUID hubId, String name, CompanyType type, String address) {
        return Company.builder()
                .hubId(hubId)
                .name(name)
                .type(type)
                .address(address)
                .build();
    }
}
