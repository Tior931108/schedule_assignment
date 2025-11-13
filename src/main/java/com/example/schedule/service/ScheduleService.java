package com.example.schedule.service;

import com.example.schedule.dto.*;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );

        // 로그인시에만 일정 작성 가능.

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
                () -> new IllegalArgumentException("존재하지 않는 일정입니다.")
        );

        // 로그인시에만 조회 가능 , 본인건만 조회 가능

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

        // 로그인 시에만 조회 가능, 중간관리자 이상만 조회 가능

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
                () -> new IllegalArgumentException("존재하지 않는 일정입니다.")
        );

        // 로그인시에만 확인가능, 일반유저는 본인 일정만 수정 가능, 중간관리자 이상은 전부 수정 가능

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
            throw new IllegalArgumentException("존재하지 않는 일정입니다.");
        }

        // 로그인시 삭제 가능, 일반유저 본인건만 삭제 가능, 중간관리자 이상은 전부 삭제 가능

        // 삭제
        scheduleRepository.deleteById(scheduleId);

    }
}
