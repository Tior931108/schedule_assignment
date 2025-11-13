package com.example.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateScheduleResponse {

    private final Long id;
    private final Long userId;
    private final String nickname;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateScheduleResponse(Long id, Long userId, String nickname, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
