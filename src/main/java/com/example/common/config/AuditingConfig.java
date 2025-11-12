package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @DateJpaTest를 위한 JpaAuditingConfig 파일
@Configuration
@EnableJpaAuditing
public class AuditingConfig {
}
