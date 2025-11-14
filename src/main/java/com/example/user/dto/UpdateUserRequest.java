package com.example.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserRequest {

    private String currentPassword;  // 현재 비밀번호 (필수)
    private String newPassword;      // 새 비밀번호 (산텍)
    private String nickname;
}
