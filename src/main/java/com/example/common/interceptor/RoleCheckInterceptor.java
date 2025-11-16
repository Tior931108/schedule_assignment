package com.example.common.interceptor;

import com.example.common.annotation.RoleRequired;
import com.example.user.dto.SessionUser;
import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleCheckInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Handler가 메서드가 아니면 통과
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // @RoleRequired 어노테이션이 있는지 확인
        RoleRequired roleRequired = handlerMethod.getMethod().getAnnotation(RoleRequired.class);

        // 어노테이션이 없으면 통과
        if(roleRequired == null){
            return true;
        }

        // 역할 체크
        return checkRole(request, response, roleRequired.value());
    }

    private boolean checkRole(HttpServletRequest request,
                              HttpServletResponse response,
                              UserRole[] requiredRoles) throws IOException {

        HttpSession session = request.getSession();

        // Filter에서 로그인 체크하고 세션이 이미 존재함.
        // 방어적으로 세션 다시한번 더 체크
        if (session == null || session.getAttribute("loginUser") == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "UNAUTHORIZED", "로그인이 필요합니다.");
            return false;
        }

        // 세션 DTO로 직렬화
        SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
        UserRole userRole = loginUser.getRole();

        // 필요한 역할 중 하나라도 가지고 있는지 확인
        boolean hasRequiredRole = Arrays.asList(requiredRoles).contains(userRole);

        // 필요한 권한이 아니면 예외처리
        if (!hasRequiredRole) {
            log.warn("권한 없는 사용자 요청: userId={}, userRole={}, requiredRoles={}, uri={}",
                    loginUser.getUserId(), userRole, Arrays.toString(requiredRoles),
                    request.getRequestURI());

            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                    "FORBIDDEN",
                    String.format("해당 기능을 사용할 권한이 없습니다. 필요 권한: %s",
                            Arrays.toString(requiredRoles)));
            return false;
        }

        // 필요한 권한이 맞다면 controller 진행
        log.info("권한 확인 성공: userId={}, role={}, requiredRoles={}, uri={}",
                loginUser.getUserId(), userRole, Arrays.toString(requiredRoles),
                request.getRequestURI());

        return true;
    }


    /**
     * 에러 응답 전송
     */
    private void sendErrorResponse(HttpServletResponse response,
                                   int status,
                                   String code,
                                   String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", code);
        errorResponse.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
