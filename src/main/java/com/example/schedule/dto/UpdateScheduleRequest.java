package com.example.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateScheduleRequest {

    @NotBlank(message = "일정 제목은 필수입니다.")
    @Size(max = 20, message = "일정 제목은 최대 30자입니다.")
    private String title;
    @NotBlank(message = "일정 내용은 필수입니다.")
    @Size(max = 20, message = "일정 내용은 최대 200자입니다.")
    private String content;
}
