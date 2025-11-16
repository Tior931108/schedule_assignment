package com.example.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    // 400 Bad Request : 클라이언트 수정
    NOT_MATCH_EMAIL(HttpStatus.BAD_REQUEST,"이메일이 일치하지 않습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다."),
    EXIST_AND_NEW_PASSWORD(HttpStatus.BAD_REQUEST,"기존 비밀번호와 다른 비밀번호를 입력해주세요."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST,"이미 사용 중인 이메일입니다."),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST,"이미 사용 중인 닉네임입니다."),
    // 401 Unauthorized : 로그인 필요(인증)
    NEED_TO_LOGIN(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다."),
    // 403 Forbidden : 권한 거부(인가)
    REJECT_AUTHORIZED(HttpStatus.FORBIDDEN,"해당 기능을 사용할 권한이 없습니다. 관리자에게 문의해주세요"),
    // 404 Not Found : 리소스가 없음
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND,"존재하지 않는 일정입니다."),
    NOT_FOUND_AUTHORIZED(HttpStatus.NOT_FOUND,"존재하지 않는 권한입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
