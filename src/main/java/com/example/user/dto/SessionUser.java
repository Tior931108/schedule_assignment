package com.example.user.dto;

import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import lombok.Getter;

import java.io.Serializable;


@Getter
// 로깅할때, JPA 엔티티를 세션에 직접 저장하면 직렬화 문제 발생 가능
// Hibernate 프록시 객체로 인한 타입 불일치
// 세션 데이터 역직렬화 시 클래스 로딩 문제 발생함. >> Serializable 구현체 적용
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String email;
    private String nickname;
    private UserRole role;

    // User 엔티티로부터 생성
    public SessionUser(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }

    // 기본 생성자 (역직렬화용)
    public SessionUser() {
    }
}
