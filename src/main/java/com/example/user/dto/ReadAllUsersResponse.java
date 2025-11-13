package com.example.user.dto;

import com.example.user.entity.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReadAllUsersResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ReadAllUsersResponse(Long id, String email, String nickname, UserRole role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
