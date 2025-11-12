package com.example.user.repository;

import com.example.common.config.AuditingConfig;
import com.example.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static com.example.user.entity.UserRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

//@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(value = AuditingConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 저장에 성공한다.")
    void saveUser() {
        // given
        // 또는 빌더 사용
         User user = User.builder()
                 .email("aaa@example.com")
                 .password("1234")
                 .name("홍길동")
                 .role(USER)
                 .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @DisplayName("이메일이 존재하는지 확인한다.")
    void existsByEmail() {
        // given
        User users = new User("aaa@example.com", "1234", "홍길동");
        userRepository.save(users);

        // when
        boolean exists = userRepository.existsByEmail("aaa@example.com");

        // then
        assertThat(exists).isTrue();
    }

}