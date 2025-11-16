package com.example.common.config;

import com.example.common.interceptor.RoleCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{

    private final RoleCheckInterceptor roleCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 역할 체크 인터셉터만 등록함.
        // 로그인 체크는 filter에서 처리
        registry.addInterceptor(roleCheckInterceptor)
                .order(1)
                .addPathPatterns("/**"); // 모든 경로
    }
}
