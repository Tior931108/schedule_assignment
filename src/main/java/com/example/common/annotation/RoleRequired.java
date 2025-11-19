package com.example.common.annotation;

import com.example.user.entity.UserRole;

import java.lang.annotation.*;

// 역할 기반 권한 커스텀 어노테이션
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleRequired {
    // 복합적인 역할이기에 허용할 역할 목록
    UserRole[] value();
}
