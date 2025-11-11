package com.example.user.entity;

import com.example.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    // 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, unique = true, nullable = false)
    private String email;
    @Column(length = 30, nullable = false)
    private String password;
    @Column(length = 30, nullable = false)
    private String name;

    // 비밀번호 제외한 생성자
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
