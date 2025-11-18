package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import com.example.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    // 닉네임명으로 필터링 (User_Nickname : 연관관계 엔티티 필드를 참조하는 JPA 표준 방식!)
    List<Schedule> findByUser_NicknameOrderByModifiedAtDesc(String nickname);
    // 일정 전체 조회 (수정일자 내림차순)
    List<Schedule> findAllByOrderByModifiedAtDesc();

    /**
     * 일정 전체 조회 (페이징 + 댓글 개수 + 유저명)
     * - LEFT JOIN으로 유저 정보 조회
     * - 서브쿼리로 댓글 개수 조회
     */
    @Query("SELECT s.id, s.title, s.content, " +
            "(SELECT COUNT(c) FROM Comment c WHERE c.scheduleId = s.id), " +
            "s.createdAt, s.modifiedAt, u.nickname " +
            "FROM Schedule s " +
            "LEFT JOIN User u ON s.user.id = u.id " +
            "ORDER BY s.modifiedAt DESC")
    Page<Object[]> findAllSchedulesWithCommentCount(Pageable pageable);

    /**
     * 유저명으로 필터링 (선택 조건)
     */
    @Query("SELECT s.id, s.title, s.content, " +
            "(SELECT COUNT(c) FROM Comment c WHERE c.scheduleId = s.id), " +
            "s.createdAt, s.modifiedAt, u.nickname " +
            "FROM Schedule s " +
            "LEFT JOIN User u ON s.user.id = u.id " +
            "WHERE u.nickname LIKE %:nickname% " +
            "ORDER BY s.modifiedAt DESC")
    Page<Object[]> findSchedulesByNicknameWithCommentCount(
            @Param("nickname") String nickname,
            Pageable pageable
    );

}
