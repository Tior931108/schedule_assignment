package com.example.common.config;

import com.example.common.filter.LoginCheckFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        // 필터 설정
        filterRegistrationBean.setFilter(new LoginCheckFilter());

        // 필터 순서 (숫자가 낮을수록 먼저 실행)
        filterRegistrationBean.setOrder(1);

        // 필터를 적용할 URL 패턴
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
