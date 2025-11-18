package com.example.comment.entity;

import com.example.common.entity.BaseEntity;
import com.example.schedule.entity.Schedule;
import com.example.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // scheduleId를 외래키로 직접 저장 (단방향) _ 일정 조회시 관련 댓글 함께 조회
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(nullable = false, length = 100)
    private String content;

    // 생성자
    public Comment(User user, Long scheduleId, String content) {
        this.user = user;
        this.scheduleId = scheduleId;
        this.content = content;
    }

    // 내용 수정 메소드
    public void update(String newContent) {
        this.content = newContent;
    }
}
