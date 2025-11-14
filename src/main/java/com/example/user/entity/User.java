package com.example.user.entity;

import com.example.common.config.PasswordEncoder;
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
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 30, unique = true, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'USER'")
    private UserRole role; // 유저 권한: USER, MANAGER, ADMIN

    // 비밀번호 포함한 생성자
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = UserRole.USER; // 기본 권한 일반유저(USER)
    }


    // 유저 비밀번호 변경
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    // 유저 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }


    // 권한 변경 메소드
    public void updateRole(UserRole newRole) {
        this.role = newRole;
    }

    // 비밀번호 체크 - 보안강화
    public boolean isPasswordMatch(String rawPassword, PasswordEncoder passwordEncoder) {
        if (this.password == null || rawPassword == null) {
            return false;
        }
        // 입력한 비밀번호와 암호롸로 저장된 비밀번호가 동일한지 확인
        return passwordEncoder.matches(rawPassword, this.password);
    }
}
