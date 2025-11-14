package com.example.user.controller;

import com.example.user.dto.LoginUserRequest;
import com.example.user.dto.LoginUserResponse;
import com.example.user.dto.SessionUser;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(
            @RequestBody LoginUserRequest request,
            HttpSession session // 세션이 없으면 새로 생성
    ) {
        log.info("로그인 시도: {}", request.getEmail());

        // 1. 이메일, 비밀번호 검증
        User user = userService.login(request.getEmail(), request.getPassword());

        // 2. Session 생성 및 유저 정보 저장
        SessionUser sessionUser = new SessionUser(user);
        // "loginUser" 라는 이름(Key)으로 sessionUser 객체(Value)를 세션 저장소에 저장
        session.setAttribute("loginUser", sessionUser);
        // 세션 유효시간 60분
        session.setMaxInactiveInterval(3600);

        log.info("로그인 성공: userId={}, email={}, role={}",
                user.getId(), user.getEmail(), user.getRole());

        // 3. 응답
        return ResponseEntity.status(HttpStatus.OK).body(new LoginUserResponse(user));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            // "loginUser"라는 이름으로 저장된 세션 속성을 찾아 sessionUser 파라미터에 주입
            @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser,
            HttpSession session) {
        if (sessionUser == null) {
            return ResponseEntity.badRequest().build();
        }

        // 세션 무효화
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 현재 로그인한 유저 정보 조회 (세션 확인용)
    @GetMapping("/present/session")
    public ResponseEntity<String> test(@SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser) {
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
