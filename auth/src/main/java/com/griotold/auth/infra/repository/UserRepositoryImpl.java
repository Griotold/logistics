package com.griotold.auth.infra.repository;

import com.griotold.auth.domain.entity.User;
import com.griotold.auth.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepositoryImpl extends JpaRepository<User, Long>, UserRepository {

    boolean existsByUsername(String username);
}
