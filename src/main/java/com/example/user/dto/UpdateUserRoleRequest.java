package com.example.user.dto;

import com.example.user.entity.UserRole;
import lombok.Getter;

@Getter
public class UpdateUserRoleRequest {

    private UserRole role;
}
