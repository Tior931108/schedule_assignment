package com.example.common.util;

import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import com.example.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    // 프로그램 시작시 자동으로 관리자 계정 생성
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User(
                    "admin@example.com",
                    passwordEncoder.encode("admin1234@!"),
                    "관리자1"
            );
            admin.updateRole(UserRole.ADMIN);
            userRepository.save(admin);
        }
    }
}
