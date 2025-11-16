package com.example.schedule.service;

import com.example.common.exception.ErrorMessage;
import com.example.common.exception.NotFoundScheduleException;
import com.example.common.exception.NotFoundUserException;
import com.example.schedule.dto.*;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
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
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 일정 저장
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest createScheduleRequest) {
        // 유저가 존재하는지 확인
        User user = userRepository.findById(createScheduleRequest.getUserId()).orElseThrow(
                () -> new NotFoundUserException(ErrorMessage.NOT_FOUND_USER)
        );

        Schedule schedule = new Schedule(
                createScheduleRequest.getTitle(),
                createScheduleRequest.getContent(),
                user
        );

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new CreateScheduleResponse(
                savedSchedule.getId(),
                savedSchedule.getUser().getId(),
                savedSchedule.getUser().getNickname(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getModifiedAt()
        );
    }

    // 일정 단건 조회
    @Transactional(readOnly = true)
    public ReadOneScheduleResponse readOneSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundScheduleException(ErrorMessage.NOT_FOUND_SCHEDULE)
        );

        // 본인건만 조회 가능 + 관리자

        // 단 건 조회 응답 (추후 댓글 포함여부 수정)
        return ReadOneScheduleResponse.from(schedule);
    }

    // 일정 전체 조회
    @Transactional(readOnly = true)
    public List<ReadAllScheduleResponse> readAllSchedule(String nickname, Integer page, Integer size) {
        List<Schedule> schedules;

        if (nickname != null && !nickname.isEmpty()) {
            // 닉네임명으로 필터링 (User_Nickname : 연관관계 엔티티 필드를 참조하는 JPA 표준 방식!)
            schedules = scheduleRepository.findByUser_NicknameOrderByModifiedAtDesc(nickname);
        } else {
            // 전체 조회
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        }

        // 응답 반환
        List<ReadAllScheduleResponse> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ReadAllScheduleResponse dto = new ReadAllScheduleResponse(
                    schedule.getId(),
                    schedule.getUser().getId(),
                    schedule.getUser().getNickname(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getCreatedAt(),
                    schedule.getModifiedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }

    // 일정 수정
    @Transactional
    public UpdateScheduleResponse updateSchedule(Long scheduleId, UpdateScheduleRequest updateScheduleRequest) {
        // 일정 존재 확인
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundScheduleException(ErrorMessage.NOT_FOUND_SCHEDULE)
        );

        //  본인 일정만 수정 및 중간관리자 이상은 전부 수정 가능

        // 제목 및 내용 수정, modifiedAt 는 Auditing에 의해 자동 갱신.
        schedule.update(updateScheduleRequest.getTitle(), updateScheduleRequest.getContent());
        scheduleRepository.flush();

        return new UpdateScheduleResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getUser().getNickname(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
        );
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        // 일정 존재 확인
        if(!scheduleRepository.existsById(scheduleId)) {
            throw new NotFoundScheduleException(ErrorMessage.NOT_FOUND_SCHEDULE);
        }

        // 로그인시 삭제 가능, 일반유저 본인건만 삭제 가능, 중간관리자 이상은 전부 삭제 가능

        // 삭제
        scheduleRepository.deleteById(scheduleId);

    }

    // URL 파라미터를 임의로 조작해서 다른 사람의 데이터에 접근하는 것 : IDOR (Insecure Direct Object Reference) 취약점 방지
//    /**
//     * 접근 권한 검증
//     * @param schedule 대상 일정
//     * @param userId 요청한 사용자 ID
//     * @param userRole 요청한 사용자 역할
//     * @param action 수행하려는 작업 (조회/수정/삭제)
//     */
//    private void validateAccess(Schedule schedule, Long userId, UserRole userRole, String action){
//        boolean isOwner = schedule.getUser().getId().equals(userId);
//        boolean isManager = userRole == UserRole.MANAGER;
//        boolean isAdmin = userRole == UserRole.ADMIN;
//
//        if (!isOwner && !isManager && !isAdmin) {
//            log.warn("권한 없는 일정 {} 시도: scheduleId={}, scheduleOwner={}, requestUser={}, role={}",
//                    action, schedule.getId(), schedule.getUser().getId(), userId, userRole);
//
//            // 커스텀 예외 처리 예정
//        }
//
//        String accessType = isOwner ? "본인" : (isAdmin ? "ADMIN" : "MANAGER");
//        log.info("일정 {} 권한 확인: scheduleId={}, userId={}, accessType={}",
//                action, schedule.getUser().getId(), userId, accessType);
//    }
}
