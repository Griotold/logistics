package com.griotold.auth.application.service;

import com.griotold.auth.application.dto.UserCreate;
import com.griotold.auth.application.dto.UserResponse;
import com.griotold.auth.application.dto.UserSignIn;
import com.griotold.auth.domain.entity.User;
import com.griotold.auth.domain.enums.Role;
import com.griotold.auth.domain.repository.UserRepository;
import com.griotold.common.exception.ErrorCode;
import com.griotold.common.exception.LogisticsException;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponse singUp(UserCreate userCreate) {
        log.info("signUp.UserCreate : {}", userCreate);

        if (userRepository.existsByUsername(userCreate.username())) {
            throw new LogisticsException(ErrorCode.DUPLICATE_USERNAME);
        }

        Role role = Role.of(userCreate.role()).orElseThrow(() -> new LogisticsException(ErrorCode.INVALID_ROLE));

        String encodedPassword = passwordEncoder.encode(userCreate.password());

        User user = User.create(userCreate.username(), encodedPassword, userCreate.email(), role);

        return UserResponse.from(userRepository.save(user));
    }

    public String singIn(UserSignIn userSignIn) {
        log.info("signIn.UserSignIn : {}", userSignIn);
        User user = userRepository.findByUsername(userSignIn.username())
                .orElseThrow(() -> new LogisticsException(ErrorCode.ENTITY_NOT_FOUND));

        validatePassword(userSignIn.password(), user.getPassword());

        return jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new LogisticsException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public Boolean verifyUser(Long id) {
        log.info("verifyUser.userId: {}", id);
        return userRepository.findById(id).isPresent();
    }

}
