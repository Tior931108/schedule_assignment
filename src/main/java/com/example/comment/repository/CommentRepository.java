package com.example.comment.repository;


import com.example.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 유저 닉네임으로 댓글 조회
    List<Comment> findByUser_NicknameOrderByModifiedAtDesc(String nickname);

    // 댓글 전체 조회
    List<Comment> findAllByOrderByModifiedAtDesc();


    // scheduleId로 댓글 목록 조회
    List<Comment> findByScheduleId(Long scheduleId);

    // scheduleId로 댓글 목록 조회 (정렬 포함)
    List<Comment> findByScheduleIdOrderByCreatedAtAsc(Long scheduleId);

    // scheduleId로 댓글 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.scheduleId = :scheduleId")
    Long countByScheduleId(@Param("scheduleId") Long scheduleId);
}
