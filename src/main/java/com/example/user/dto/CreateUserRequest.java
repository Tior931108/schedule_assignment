package com.example.user.dto;

import com.example.user.entity.UserRole;
import lombok.Getter;

@Getter
public class CreateUserRequest {

    private String email;
    private String password;
    private String nickname;

}
