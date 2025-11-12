package com.example.user.entity;

// 유저 권한 Enum
public enum UserRole {
    USER,       // 일반 유저 : 본인이 작성한 data CRUD
    MANAGER,    // 중간관리자 : 일반 유저 기능 + 모든 data CRUD 기능
    ADMIN       // 최고관리자 : 중간관리자 기능 + 계정 권한 변경 가능
}
