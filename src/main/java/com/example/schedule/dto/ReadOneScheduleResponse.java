package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReadOneScheduleResponse {

    private final Long id;
    private final Long userId;
    private final String nickname;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ReadOneScheduleResponse(Long id, Long userId, String nickname, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ReadOneScheduleResponse from(Schedule schedule) {
        return new ReadOneScheduleResponse(
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
