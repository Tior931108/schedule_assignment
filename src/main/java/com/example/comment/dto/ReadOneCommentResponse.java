package com.example.comment.dto;

import com.example.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReadOneCommentResponse {

    private final Long id;
    private final Long scheduleId;
    private final Long userId;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ReadOneCommentResponse(Long id, Long scheduleId, Long userId, String nickname, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // entity > dto 변환
    public static ReadOneCommentResponse from(Comment comment) {
        return new ReadOneCommentResponse(
                comment.getId(),
                comment.getScheduleId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
