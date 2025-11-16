package com.example.common.annotation;

import com.example.user.entity.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 역할 기반 권한 커스텀 어노테이션
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleRequired {
    // 복합적인 역할이기에 허용할 역할 목록
    UserRole[] value();
}
