package com.example.schedule.dto;

import com.example.user.entity.User;
import lombok.Getter;

@Getter
public class CreateScheduleRequest {

    private Long userId;
    private String title;
    private String content;
}
