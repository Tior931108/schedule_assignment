package com.example.user.entity;

import com.example.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder  // 테스트 연습
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Builder를 위해 필요!
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

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'USER'")
    private UserRole role; // 유저 권한: USER, MANAGER, ADMIN

    // 비밀번호 포함한 생성자
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = UserRole.USER; // 기본 권한 일반유저(USER)
    }


    // 유저 정보 수정
    public void update(String password, String name) {
        this.password = password;
        this.name = name;
    }

    // 최고관리자 권한 부여 메서드
    public void updateToAdmin() {
        this.role = UserRole.ADMIN;
    }

    // 중간관리자 권한 부여 메서드
    public void updateToManager() {
        this.role = UserRole.MANAGER;
    }

    // 일반 회원으로 권한 변경
    public void updateToUser() {
        this.role = UserRole.USER;
    }

    // 비밀번호 체크
    public boolean isPasswordMatch(String password) {
        if (this.password == null || password == null) {
            return false;
        }
        return this.password.equals(password);
    }
}
