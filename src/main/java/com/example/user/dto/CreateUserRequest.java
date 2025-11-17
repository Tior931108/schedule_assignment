package com.example.user.dto;

import com.example.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateUserRequest {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    // 비밀번호 (복잡한 규칙)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 영문, 숫자를 포함한 8자 이상이어야 합니다")
    private String password;
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 20, message = "닉네임은 최대 30자입니다.")
    private String nickname;

}
