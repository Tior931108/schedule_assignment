package com.example.user.service;

import com.example.user.dto.*;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 생성 - 회원가입, 로그인 인증X
    @Transactional
    public CreateUserResponse save(CreateUserRequest createUserRequest) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        // 닉네임 중복 체크
        if(userRepository.existsByNickname(createUserRequest.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 요청으로 들어온 속성값
        User user = new User(
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
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
    public ReadOneUserResponse readOneUser(Long userId) {
        // 가입된 유저인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        // 로그인 상태 확인 , 본인것만 조회 가능

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

    // 유저 수정
    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        // 가입된 유저인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        // 로그인 상태인지 확인 , 본인 정보만 수정 가능

        // 현재 비밀번호 확인
        if (!user.isPasswordMatch(updateUserRequest.getCurrentPassword())) {
            throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호가 입력된 경우
        if (updateUserRequest.getNewPassword() != null && !updateUserRequest.getNewPassword().isEmpty()) {
            // 기존 비밀번호와 동일한지 체크
            if (user.isPasswordMatch(updateUserRequest.getNewPassword())) {
                throw new IllegalStateException("기존 비밀번호와 다른 비밀번호를 입력해주세요.");
            }
            // 작성일, 수정일은 Auditing에 의해 자동 변경
            user.update(updateUserRequest.getNewPassword(), updateUserRequest.getNickname());
        }

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
    public void deleteUser(Long userId) {
        // 가입된 유저인지 확인
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        // 로그인 상태인지 확인, 본인 정보만 삭제 가능

        userRepository.delete(user);

    }

}
