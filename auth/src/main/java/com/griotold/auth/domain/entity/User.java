package com.griotold.auth.domain.entity;

import com.griotold.auth.domain.enums.Role;
import com.griotold.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_user")
public class User extends BaseEntity {
    // username
    // password
    // nickname
    // slack_id
    // role
    // 감사 로그, isDeleted
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public static User create(String username, String password, String email, Role role) {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .build();
    }
}
