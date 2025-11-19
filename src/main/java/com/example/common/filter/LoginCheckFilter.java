package com.example.common.filter;

import com.example.common.exception.ErrorMessage;
import com.example.common.exception.NeedToLoginException;
import com.example.user.dto.SessionUser;
import com.example.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginCheckFilter implements Filter {

    // 로그인 체크를 하지 않을 URL 패턴
    private static final String[] WHITE_LIST = {
            "/auth/register",   // 회원가입
            "/auth/login",      // 로그인
            "/error",               // 에러
//            "/favicon.ico",
//            "/css/*",
//            "/js/*",
//            "/images/*"
    };

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestURI = httpServletRequest.getRequestURI();

        try {
            // 화이트 리스트 체크
            if (isLoginCheckPath(requestURI)) {

                HttpSession session = httpServletRequest.getSession();

                // 세션이 없거나 로그인 정보가 없는 경우
                if (session == null || session.getAttribute("loginUser") == null) {
                    log.warn("미인증 사용자 요청: {}", requestURI);

                    // 401 에러 응답
                    sendErrorResponse(httpServletResponse,
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "NEED_TO_LOGIN",
                            "로그인이 필요합니다.");
//                    throw new NeedToLoginException(ErrorMessage.NEED_TO_LOGIN);

                    // 다음 필터나 서블릿으로 진행하지 않음
                    return;
                }

                SessionUser loginUser = (SessionUser) session.getAttribute("loginUser");
                log.info("인증된 사용자: userId={}, role={}",
                        loginUser.getUserId(), loginUser.getRole());
            }

            // 다음 필터 또는 서블릿으로 진행
            chain.doFilter(request, response);

        } catch (Exception e) {
            log.error("로그인 필터 예외 발생: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * 화이트리스트에 없는 경로인지 확인 (로그인 체크 필요 여부)
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

    /**
     * 에러 응답 전송
     * <p>
     * filter의 경우, Spring Context 범위 바깥의 Web Context 영역이라서 전역에러 어노테이션의 범위 밖임
     * 따로 에러응답을 만들어야함.
     */
    private void sendErrorResponse(HttpServletResponse response,
                                   int status,
                                   String code,
                                   String message) throws IOException {

        response.setStatus(status);
        // Spring 컨텍스트 @ExceptionHandler의 경우 리턴할때 JSON 객체로 자동응답해주지만,
        // filter 응답의 경우 아래 JSON 타입 및 UTF-8 한글 변환설정해주어야함.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", code);
        errorResponse.put("status", response.getStatus());
        errorResponse.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
