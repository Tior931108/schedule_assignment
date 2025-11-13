package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    // 닉네임명으로 필터링 (User_Nickname : 연관관계 엔티티 필드를 참조하는 JPA 표준 방식!)
    List<Schedule> findByUser_NicknameOrderByModifiedAtDesc(String nickname);
    // 일정 전체 조회 (수정일자 내림차순)
    List<Schedule> findAllByOrderByModifiedAtDesc();
}
