package com.example.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

    private Long userId;
    @NotBlank(message = "일정 내용은 필수입니다.")
    @Size(max = 20, message = "일정 내용은 최대 200자입니다.")
    private String content;
}
