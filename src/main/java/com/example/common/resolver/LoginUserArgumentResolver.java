package com.example.common.resolver;

import com.example.common.annotation.LoginUser;
import com.example.common.exception.ErrorMessage;
import com.example.common.exception.NeedToLoginException;
import com.example.user.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    /**
     * @LoginUser 어노테이션이 있고, SessionUser 타입인 파라미터를 지원
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean isSessionUserType = SessionUser.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginUserAnnotation && isSessionUserType;
    }

    /**
     * 세션에서 SessionUser를 꺼내서 반환
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // 세션 정보 확인
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("loginUser");

        if (sessionUser == null) {
            throw new NeedToLoginException(ErrorMessage.NEED_TO_LOGIN);
        }

        log.debug("ArgumentResolver: userId={}, role={}",
                sessionUser.getUserId(), sessionUser.getRole());

        return sessionUser;
    }
}
