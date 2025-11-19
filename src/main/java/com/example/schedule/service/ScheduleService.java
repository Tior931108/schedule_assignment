package com.example.schedule.service;

import com.example.comment.dto.ReadOneCommentResponse;
import com.example.comment.repository.CommentRepository;
import com.example.common.exception.*;
import com.example.common.util.AccessValidator;
import com.example.schedule.dto.*;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import com.example.user.dto.SessionUser;
import com.example.user.entity.User;
import com.example.user.entity.UserRole;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AccessValidator accessValidator;

    // 일정 저장
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest createScheduleRequest, SessionUser sessionUser) {
        // 유저가 존재하는지 확인
        User user = userRepository.findById(createScheduleRequest.getUserId()).orElseThrow(
                () -> new NotFoundUserException(ErrorMessage.NOT_FOUND_USER)
        );

        // request 유저 ID와 세션 유저 ID가 다른 경우 예외처리
        if (!Objects.equals(createScheduleRequest.getUserId(), sessionUser.getUserId())) {
            throw new OnlyOwnerAccessException(ErrorMessage.ONLY_OWNER_ACCESS);
        }

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
    public ReadOneScheduleResponse readOneSchedule(Long scheduleId, SessionUser sessionUser) {
        Schedule schedule = getSchedule(scheduleId);

        // 본인건만 조회 가능 + 관리자
        accessValidator.validateScheduleAccess(
                schedule.getUser().getId(),
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "조회"
        );

        // 댓글 목록 조회 (Stream 활용)
        List<ReadOneCommentResponse> comments = commentRepository.findByScheduleIdOrderByCreatedAtAsc(scheduleId)
                .stream()
                .map(ReadOneCommentResponse::from)
                .toList();

        log.info("일정 조회: scheduleId={}, commentCount={}", scheduleId, comments.size());

        // 단 건 일정 조회 및 관련 댓글 함께 응답
        return ReadOneScheduleResponse.of(schedule, comments);
    }

    // 일정 전체 조회
    @Transactional(readOnly = true)
    public Page<ReadAllScheduleResponse> readAllSchedule(String nickname, Integer page, Integer size) {
        // 기본값 설정
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? size : 10;

        // Pageable 생성 (페이지 번호 0부터 시작)
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Object[]> results;

        if (nickname != null && !nickname.isEmpty()) {
            // 닉네임명으로 필터링
            results = scheduleRepository.findSchedulesByNicknameWithCommentCount(nickname, pageable);
        } else {
            // 전체 조회
            results = scheduleRepository.findAllSchedulesWithCommentCount(pageable);
        }

        // object[]를 DTO로 변환
        return results.map(ReadAllScheduleResponse::convertToDto);
    }

    // 일정 수정
    @Transactional
    public UpdateScheduleResponse updateSchedule(Long scheduleId, UpdateScheduleRequest updateScheduleRequest, SessionUser sessionUser) {
        // 일정 존재 확인
        Schedule schedule = getSchedule(scheduleId);

        //  본인 일정만 수정 및 중간관리자 이상은 전부 수정 가능
        accessValidator.validateScheduleAccess(
                schedule.getUser().getId(),
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "수정"
        );

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
    public void deleteSchedule(Long scheduleId, SessionUser sessionUser) {
        // 일정 존재 확인
        Schedule schedule = getSchedule(scheduleId);

        // 일반유저 본인건만 삭제 가능, 중간관리자 이상은 전부 삭제 가능
        accessValidator.validateScheduleAccess(
                schedule.getUser().getId(),
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "삭제"
        );

        // 삭제
        scheduleRepository.deleteById(scheduleId);

    }

    // 일정 존재 확인 메소드
    public Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundScheduleException(ErrorMessage.NOT_FOUND_SCHEDULE)
        );
    }
}
