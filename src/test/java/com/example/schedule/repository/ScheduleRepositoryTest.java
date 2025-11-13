package com.example.schedule.repository;

import com.example.common.config.AuditingConfig;
import com.example.schedule.entity.Schedule;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(value = AuditingConfig.class)
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("일정에서 유저 닉네임으로 조회되는지 확인한다.")
    void findByUser_NicknameOrderByModifiedAtDesc() {
        // given
        User user = new User("aaa@example.com", "1234", "홍길동");
        userRepository.save(user);

        Schedule schedule = new Schedule("테스트제목","테스트 내용", user);
        scheduleRepository.save(schedule);

        // when
        List<Schedule> schedules = scheduleRepository.findByUser_NicknameOrderByModifiedAtDesc("홍길동");

        // then
        assertThat(schedules).isNotNull();
    }
}