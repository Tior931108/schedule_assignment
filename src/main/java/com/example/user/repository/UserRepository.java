package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일 중복체크
    boolean existsByEmail(String email);
    // 닉네임 중복체크
    boolean existsByNickname(String nickname);
    // 이메일 확인체크
    Optional<User> findByEmail(String email);
}
