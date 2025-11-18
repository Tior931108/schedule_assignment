package com.example.common.annotation;

import java.lang.annotation.*;

// 세션 정보를 Service로 넘기기 위한 커스텀 어노테이션 _ 파라미터
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
