package com.example.user.dto;

import com.example.common.annotation.EnumValidator;
import com.example.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserRoleRequest {

    @NotBlank(message = "권한은 필수입니다.")
    @EnumValidator(
            targetEnum = UserRole.class,
            message = "유저 권한은 USER, MANAGER, ADMIN 중에 하나입니다."
    )
    private String role;  // String으로 받기

    /**
     * Validation 통과 후 String을 UserRole Enum으로 변환
     */
    public UserRole getUserRole() {
        return UserRole.valueOf(role.toUpperCase());
    }
}
