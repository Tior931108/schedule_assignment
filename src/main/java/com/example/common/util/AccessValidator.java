package com.example.common.util;

import com.example.common.exception.ErrorMessage;
import com.example.common.exception.RejectAuthorizedException;
import com.example.user.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccessValidator {

    /**
     * 일정 접근 권한 검증
     */
    public void validateScheduleAccess(Long ownerId, Long userId, UserRole userRole, String action) {
        validateAccess(ownerId, userId, userRole, action, "일정");
    }

    public void validateUserAccess(Long ownerId, Long userId, UserRole userRole, String action) {
        validateAccess(ownerId, userId, userRole, action, "유저");
    }

    /**
     * 공통 권한 검증 로직
     */
    private void validateAccess(Long ownerId, Long userId, UserRole userRole,
                                String action, String resourceType) {
        // Guard Clause 패턴 적용!
        if (isOwner(ownerId, userId)) {
            logSuccess(action, userId, "본인", resourceType);
            return;
        }

        if (hasAdminRole(userRole)) {
            logSuccess(action, userId, userRole.name(), resourceType);
            return;
        }

        // 권한 없음 _ 관리자는 대부분 통과이기에 본인정보만 접근 가능한걸로 안내
        logDenied(action, ownerId, userId, userRole, resourceType);
        throw new RejectAuthorizedException(ErrorMessage.ONLY_OWNER_ACCESS);
    }

    /**
     * 소유자 확인
     */
    private boolean isOwner(Long ownerId, Long userId) {
        return ownerId.equals(userId);
    }

    /**
     * 관리자 권한 확인
     */
    private boolean hasAdminRole(UserRole userRole) {
        return userRole == UserRole.ADMIN || userRole == UserRole.MANAGER;
    }

    /**
     * 성공 로그
     */
    private void logSuccess(String action, Long userId, String accessType, String resourceType) {
        log.info("{} {} 권한 확인: userId={}, accessType={}",
                resourceType, action, userId, accessType);
    }

    /**
     * 실패 로그
     */
    private void logDenied(String action, Long ownerId, Long userId,
                           UserRole userRole, String resourceType) {
        log.warn("권한 없는 {} {} 시도: ownerId={}, requestUserId={}, role={}",
                resourceType, action, ownerId, userId, userRole);
    }
}
