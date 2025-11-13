package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import com.example.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateScheduleResponse {

    private final Long id;
    private final Long userId;
    private final String nickname;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CreateScheduleResponse(Long id, Long userId, String nickname, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // Schedule Entity로부터 생성하는 from 메서드
    public static CreateScheduleResponse from(Schedule schedule) {
        return new CreateScheduleResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getUser().getNickname(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }
}
