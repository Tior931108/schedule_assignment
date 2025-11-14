package com.example.user.dto;

import com.example.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LoginUserResponse {

    private Long userId;
    private String email;
    private String nickname;
    private String role; // "USER", "MANAGER", "ADMIN"
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public LoginUserResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
