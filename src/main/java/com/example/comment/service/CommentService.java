package com.example.comment.service;

import com.example.comment.dto.*;
import com.example.comment.entity.Comment;
import com.example.comment.repository.CommentRepository;
import com.example.common.exception.*;
import com.example.common.util.AccessValidator;
import com.example.schedule.dto.ReadAllScheduleResponse;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import com.example.user.dto.SessionUser;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AccessValidator accessValidator;

    // 댓글 생성
    @Transactional
    public CreateCommentResponse save(Long scheduleId, CreateCommentRequest createCommentRequest, SessionUser sessionUser) {
        // 일정이 있는지 확인
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundScheduleException(ErrorMessage.NOT_FOUND_SCHEDULE)
        );

        // 존재하는 유저인지 확인
        User user = userRepository.findById(createCommentRequest.getUserId()).orElseThrow(
                () -> new NotFoundUserException(ErrorMessage.NOT_FOUND_USER)
        );

        // request 유저 ID와 세션 유저 ID가 다른 경우 예외처리
        if (!Objects.equals(createCommentRequest.getUserId(), sessionUser.getUserId())) {
            throw new OnlyOwnerAccessException(ErrorMessage.ONLY_OWNER_ACCESS);
        }

        Comment comment = new Comment(
                user,
                schedule.getId(),
                createCommentRequest.getContent()
        );

        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(
                savedComment.getId(),
                savedComment.getScheduleId(),
                savedComment.getUser().getId(),
                savedComment.getUser().getNickname(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }

    // 댓글 단건 조회
    @Transactional(readOnly = true)
    public ReadOneCommentResponse readOneComment(Long scheduleId, Long commentId, SessionUser sessionUser) {
        // 존재하는 일정인지 확인
        existsBySchedule(scheduleId);

        // 존재하는 댓글인지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundCommentException(ErrorMessage.NOT_FOUND_COMMENT)
        );

        // 본인이 작성한 댓글만 조회 + 관리자
        accessValidator.validateCommentAccess(
                comment.getUser().getId(),
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "조회"
        );

        return ReadOneCommentResponse.from(comment);
    }

    // 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<ReadAllCommentsResponse> readAllComments(String nickname) {
        List<Comment> comments;

        if (nickname != null && !nickname.isEmpty()) {
            // 닉네임명으로 필터링 (User_Nickname : 연관관계 엔티티 필드를 참조하는 JPA 표준 방식!)
            comments = commentRepository.findByUser_NicknameOrderByModifiedAtDesc(nickname);
        } else {
            // 전체 조회
            comments = commentRepository.findAllByOrderByModifiedAtDesc();
        }

        // 응답 반환
        List<ReadAllCommentsResponse> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            ReadAllCommentsResponse dto = new ReadAllCommentsResponse(
                    comment.getId(),
                    comment.getScheduleId(),
                    comment.getUser().getId(),
                    comment.getUser().getNickname(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt()
            );
            dtos.add(dto);
        }
        return dtos;
    }

    // 댓글 수정
    @Transactional
    public UpdateCommentResponse updateComment(Long scheduleId, Long commentId, UpdateCommentRequest updateCommentRequest, SessionUser sessionUser) {
        // 존재하는 일정인지 확인
        existsBySchedule(scheduleId);

        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundCommentException(ErrorMessage.NOT_FOUND_COMMENT)
        );

        // 본인이 작성한 댓글만 수정 + 관리자
        accessValidator.validateCommentAccess(
                comment.getUser().getId(),
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "수정"
        );

        // 내용 수정
        comment.update(updateCommentRequest.getContent());
        commentRepository.flush();

        return new UpdateCommentResponse(
                comment.getId(),
                comment.getScheduleId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long scheduleId, Long commentId, SessionUser sessionUser) {
        // 존재하는 일정인지 확인
        existsBySchedule(scheduleId);

        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundCommentException(ErrorMessage.NOT_FOUND_COMMENT)
        );

        // 본인 댓글만 삭제 + 관리자
        accessValidator.validateCommentAccess(
                comment.getUser().getId(),
                sessionUser.getUserId(),
                sessionUser.getRole(),
                "삭제"
        );

        // 삭제
        commentRepository.delete(comment);

    }

    // 존재하는 일정인지 확인
    public void existsBySchedule(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new NotFoundScheduleException(ErrorMessage.NOT_FOUND_SCHEDULE);
        }
    }
}
