package com.example.user.controller;


import com.example.user.dto.*;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 생성 (회원가입)
    @PostMapping("/auth/register")
    public ResponseEntity<CreateUserResponse> createUser (
            @RequestBody CreateUserRequest createUserRequest) {
        CreateUserResponse result = userService.register(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 유저 단건 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<ReadOneUserResponse> ReadOneUser (
            @PathVariable Long userId){
        ReadOneUserResponse result = userService.readOneUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 유저 전체 조회
    @GetMapping("/users")
    public ResponseEntity<List<ReadAllUsersResponse>> ReadAllUsers (
            // 페이징 처리를 위한 RequestParam (page : 페이지 번호, size : 한 페이지당 최대로 보이는 유저 수)
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        List<ReadAllUsersResponse> results = userService.findAll(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    // 유저 정보 수정
    @PatchMapping("/users/{userId}")
    public ResponseEntity<UpdateUserResponse> updateUser (
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest updateUserRequest) {
        UpdateUserResponse result = userService.updateUser(userId, updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 유저 권한 수정
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<UpdateUserRoleResponse> updateUserRole (
            @PathVariable Long userId,
            @RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        UpdateUserRoleResponse result = userService.updateUserRole(userId, updateUserRoleRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 유저 삭제 (회원 탈퇴)
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser (
            @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
