package com.example.common.config;

import com.example.common.interceptor.RoleCheckInterceptor;
import com.example.common.resolver.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RoleCheckInterceptor roleCheckInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 역할 체크 인터셉터만 등록함.
        // 로그인 체크는 filter에서 처리
        registry.addInterceptor(roleCheckInterceptor)
                .order(1)
                .addPathPatterns("/**"); // 모든 경로
    }

    // ArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
