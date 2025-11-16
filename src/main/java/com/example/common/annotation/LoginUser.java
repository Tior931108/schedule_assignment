package com.example.common.annotation;

import java.lang.annotation.*;

// 세션 정보를 Service로 넘기기 위한 커스텀 어노테이션 _ 파라미터
// 서비스에서 본인 정보만 접근할 수 있도록함.
// URL 파라미터를 임의로 조작해서 다른 사람의 데이터에 접근하는 것 : IDOR (Insecure Direct Object Reference) 취약점 방지
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
