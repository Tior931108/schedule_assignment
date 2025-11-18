package com.example.schedule.dto;

import com.example.comment.dto.ReadOneCommentResponse;
import com.example.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReadOneScheduleResponse {

    private final Long id;
    private final Long userId;
    private final String nickname;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private List<ReadOneCommentResponse> comments;  // 댓글 목록
    private Integer commentCount;  // 댓글 개수

    public ReadOneScheduleResponse(Long id, Long userId, String nickname, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<ReadOneCommentResponse> comments, Integer commentCount) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.comments = comments;
        this.commentCount = commentCount;
    }

    public static ReadOneScheduleResponse of(Schedule schedule, List<ReadOneCommentResponse> comments) {
        return new ReadOneScheduleResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getUser().getNickname(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt(),
                comments,
                comments.size()
        );
    }
}
