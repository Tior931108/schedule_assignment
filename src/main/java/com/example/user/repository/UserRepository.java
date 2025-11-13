package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일 중복체크
    boolean existsByEmail(String email);
    // 닉네임 중복체크
    boolean existsByNickname(String nickname);
}
