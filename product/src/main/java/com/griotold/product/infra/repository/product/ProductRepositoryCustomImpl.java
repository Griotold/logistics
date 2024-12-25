package com.griotold.product.infra.repository.product;

import com.griotold.common.exception.ErrorCode;
import com.griotold.common.exception.LogisticsException;
import com.griotold.product.domain.entity.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.UUID;

import static com.griotold.product.domain.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> search(UUID companyId, String name, Pageable pageable) {
        BooleanBuilder booleanBuilder = toBooleanBuilder(companyId, name);
        List<Product> contents = queryFactory.selectFrom(product)
                .where(booleanBuilder)
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Product> count = queryFactory.select(product)
                .from(product)
                .where(booleanBuilder);

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchCount);
    }

    private BooleanBuilder toBooleanBuilder(UUID companyId, String name) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(eqCompanyId(companyId));
        booleanBuilder.and(likeName(name));
        return booleanBuilder;
    }

    private BooleanExpression eqCompanyId(UUID companyId) {
        return companyId != null ? product.companyId.eq(companyId) : null;
    }

    private BooleanExpression likeName(String name) {
        String condition = name == null ? "" : name;
        return product.name.like("%" + condition + "%");
    }

    private BooleanExpression isNotDeleted() {
        return product.isDeleted.isFalse();
    }

    // Pageable 의 Sort 객체를 기반으로 QueryDSL OrderSpecifier 배열을 생성하는 메서드
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return pageable.getSort().stream()
                .map(order -> {
                    ComparableExpressionBase<?> sortPath = getSortPath(order.getProperty());
                    return new OrderSpecifier<>(
                            order.isAscending()
                                    ? com.querydsl.core.types.Order.ASC
                                    : com.querydsl.core.types.Order.DESC,
                            sortPath);
                })
                .toArray(OrderSpecifier[]::new);
    }

    // 정렬 기준
    private ComparableExpressionBase<?> getSortPath(String property) {
        return switch (property) {
            case "createdAt" -> product.createdAt;
            case "updatedAt" -> product.updatedAt;
            case "name" -> product.name;
            default -> throw new LogisticsException(ErrorCode.UNSUPPORTED_SORT_TYPE);
        };
    }
}
