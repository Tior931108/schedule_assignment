package com.example.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateCommentResponse {

    private final Long id;
    private final Long scheduleId;
    private final Long userId;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public UpdateCommentResponse(Long id, Long scheduleId, Long userId, String nickname, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
