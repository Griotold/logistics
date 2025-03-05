package com.griotold.auth.domain.repository;

import com.griotold.auth.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    User save(User product);

    <S extends User> List<S> saveAll(Iterable<S> entities);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
