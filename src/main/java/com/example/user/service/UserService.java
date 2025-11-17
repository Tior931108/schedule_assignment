package com.example.user.service;

import com.example.common.util.PasswordEncoder;
import com.example.common.exception.*;
import com.example.common.util.AccessValidator;
import com.example.user.dto.*;
import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessValidator accessValidator;

    // 로그인 체크
    @Transactional
    public User login(String email, String rawPassword) {
        // 이메일 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotMatchEmailException(ErrorMessage.NOT_MATCH_EMAIL));

        // 비밀번호 확인
        if (!user.isPasswordMatch(rawPassword, passwordEncoder)) {
            throw new NotMatchPasswordException(ErrorMessage.NOT_MATCH_PASSWORD);
        }

        return user;
    }


    // 유저 생성 - 회원가입, 로그인 인증X
    @Transactional
    public CreateUserResponse register(CreateUserRequest createUserRequest) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new ExistEmailException(ErrorMessage.EXIST_EMAIL);
        }
        // 닉네임 중복 체크
        if(userRepository.existsByNickname(createUserRequest.getNickname())) {
            throw new ExistNicknameException(ErrorMessage.EXIST_NICKNAME);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(createUserRequest.getPassword());

        // 요청으로 들어온 속성값
        User user = new User(
                createUserRequest.getEmail(),
                encodedPassword,
                createUserRequest.getNickname()
        );

        // repository 저장
        User savedUser = userRepository.save(user);

        // controller로 반환
        return new CreateUserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getNickname(),
                savedUser.getRole(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }

    // 유저 단건 조회
    @Transactional(readOnly = true)
    public ReadOneUserResponse readOneUser(Long userId, SessionUser sessionUser) {
        // 가입된 유저인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundUserException(ErrorMessage.NOT_FOUND_USER)
        );

        // 본인것만 조회 가능 + 관리자
        accessValidator.validateUserAccess(
                userId,
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "조회"
        );

        return new ReadOneUserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    // 유저 전체 조회
    @Transactional(readOnly = true)
    public List<ReadAllUsersResponse> findAll(Integer page, Integer size) {
        // 로그인 상태 확인 및, 중간관리자 이상만 유저 전체 조회 가능

        // 유저 전체 조회
        List<User> users = userRepository.findAll();

        List<ReadAllUsersResponse> dtos = new ArrayList<>();

        for (User user : users) {
            ReadAllUsersResponse dto = new ReadAllUsersResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getRole(),
                    user.getCreatedAt(),
                    user.getModifiedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }

    // 유저 정보 수정
    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest, SessionUser sessionUser) {
        // 가입된 유저인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundUserException(ErrorMessage.NOT_FOUND_USER)
        );

        // 본인 정보만 수정 가능 + 관리자
        accessValidator.validateUserAccess(
                userId,
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "수정"
        );

        // 현재 비밀번호 확인
        if (!user.isPasswordMatch(updateUserRequest.getCurrentPassword(), passwordEncoder)) {
            throw new NotMatchPasswordException(ErrorMessage.NOT_MATCH_PASSWORD);
        }

        // 새 비밀번호가 입력된 경우
        if (updateUserRequest.getNewPassword() != null && !updateUserRequest.getNewPassword().isEmpty()) {
            // 기존 비밀번호와 동일한지 체크
            if (user.isPasswordMatch(updateUserRequest.getNewPassword(), passwordEncoder)) {
                throw new ExistAndNewPasswordException(ErrorMessage.EXIST_AND_NEW_PASSWORD);
            }

            String encodedPassword = passwordEncoder.encode(updateUserRequest.getNewPassword());
            // 비밀번호 변경
            user.updatePassword(encodedPassword);
        }

        // 닉네임 변경
        user.updateNickname(updateUserRequest.getNickname());

        // 수정일자 명시적 flush 선언
        userRepository.flush();

        // 응답 DTO
        return new UpdateUserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    // 유저 삭제
    @Transactional
    public void deleteUser(Long userId, SessionUser sessionUser) {
        // 가입된 유저인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundUserException(ErrorMessage.NOT_FOUND_USER)
        );

        // 본인 정보만 삭제 가능 + 관리자
        accessValidator.validateUserAccess(
                userId,
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "삭제"
        );

        userRepository.delete(user);

    }

    // 유저 권한 수정
    @Transactional
    public UpdateUserRoleResponse updateUserRole(Long userId, UpdateUserRoleRequest updateUserRoleRequest) {
        // 가입된 유저인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundUserException(ErrorMessage.NOT_FOUND_USER)
        );

        // 입력된 권한이 USER, MANAGER, ADMIN중에 있는지 확인
        UserRole userRole = updateUserRoleRequest.getUserRole();
        log.info("userRole : {}", userRole.name());

        if(!(userRole.name().equals("USER") || userRole.name().equals("MANAGER") ||  userRole.name().equals("ADMIN")) ) {
            throw new NotFoundAuthorizedException(ErrorMessage.NOT_FOUND_AUTHORIZED);
        }

        // Enum 권한명이 맞다면 변경
        user.updateRole(updateUserRoleRequest.getUserRole());


        // 수정일자 명시적 flush 선언
        userRepository.flush();

        return new UpdateUserRoleResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
