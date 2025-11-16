package com.example.schedule.controller;

import com.example.common.annotation.LoginUser;
import com.example.common.annotation.RoleRequired;
import com.example.schedule.dto.*;
import com.example.schedule.entity.Schedule;
import com.example.schedule.service.ScheduleService;
import com.example.user.dto.SessionUser;
import com.example.user.entity.UserRole;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> createSchedule(
            @RequestBody CreateScheduleRequest createScheduleRequest) {
        CreateScheduleResponse result = scheduleService.save(createScheduleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 일정 단건 조회
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ReadOneScheduleResponse> readOneSchedule(
            @PathVariable("scheduleId") Long scheduleId,
            @LoginUser SessionUser sessionUser) {
        ReadOneScheduleResponse result = scheduleService.readOneSchedule(scheduleId, sessionUser);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 일정 전체조회
    // 중간관리자, 최고관리자만 조회 가능
    @RoleRequired({UserRole.MANAGER, UserRole.ADMIN})
    @GetMapping("/schedules")
    public ResponseEntity<List<ReadAllScheduleResponse>> readAllSchedule(
            @RequestParam(required = false) String nickname,
            // 페이징 처리를 위한 RequestParam (page : 페이지 번호, size : 한 페이지당 최대로 보이는 일정 갯수)
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        List<ReadAllScheduleResponse> results = scheduleService.readAllSchedule(nickname, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    // 일정 수정
    @PatchMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
            @PathVariable("scheduleId") Long scheduleId,
            @RequestBody UpdateScheduleRequest updateScheduleRequest,
            @LoginUser SessionUser sessionUser) {
        UpdateScheduleResponse result = scheduleService.updateSchedule(scheduleId, updateScheduleRequest, sessionUser);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 일정 삭제
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable("scheduleId") Long scheduleId,
            @LoginUser SessionUser sessionUser) {
        scheduleService.deleteSchedule(scheduleId, sessionUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
