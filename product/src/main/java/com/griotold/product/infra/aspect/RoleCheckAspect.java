package com.griotold.product.infra.aspect;

import com.griotold.common.exception.ErrorCode;
import com.griotold.common.exception.LogisticsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private final HttpServletRequest request;

    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        // 헤더에서 role 정보 가져오기
        String role = request.getHeader("X-Role");
        if (role == null) {
            throw new LogisticsException(ErrorCode.MISSING_ROLE);
        }

        log.info("Checking role {}", role);
        // 허용된 권한 체크
        List<String> allowedRoles = List.of(requireRole.value());
        if (!allowedRoles.contains(role)) {
            throw new LogisticsException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}
