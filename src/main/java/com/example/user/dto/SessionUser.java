package com.example.user.dto;

import com.example.user.entity.User;
import lombok.Getter;

@Getter
public class SessionUser {

    private final String email;
    private final String password;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
