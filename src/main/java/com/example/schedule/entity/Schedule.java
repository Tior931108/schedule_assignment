package com.example.schedule.entity;

import com.example.common.entity.BaseEntity;
import com.example.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false)
    private String title;
    @Column(length = 200, nullable = false)
    private String content;

    // 단방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 생성자
    public Schedule(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 제목 및 내용 수정 메소드
    public void update(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
    }
}
