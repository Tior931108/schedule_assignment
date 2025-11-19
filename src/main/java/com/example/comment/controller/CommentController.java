package com.example.comment.controller;

import com.example.comment.dto.*;
import com.example.comment.service.CommentService;
import com.example.common.annotation.LoginUser;
import com.example.common.annotation.RoleRequired;
import com.example.schedule.dto.UpdateScheduleResponse;
import com.example.user.dto.SessionUser;
import com.example.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules/{scheduleId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/comments")
    public ResponseEntity<CreateCommentResponse> createComment(
            @PathVariable("scheduleId") Long scheduleId,
            @RequestBody CreateCommentRequest createCommentRequest,
            @LoginUser SessionUser sessionUser) {
        CreateCommentResponse result = commentService.save(scheduleId, createCommentRequest, sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 댓글 단건 조회
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<ReadOneCommentResponse> readOneComment(
            @PathVariable("scheduleId") Long scheduleId,
            @PathVariable("commentId") Long commentId,
            @LoginUser SessionUser sessionUser) {
        ReadOneCommentResponse result = commentService.readOneComment(scheduleId, commentId, sessionUser);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 댓글 전체 조회
    @RoleRequired({UserRole.MANAGER, UserRole.ADMIN})
    @GetMapping("/comments")
    public ResponseEntity<List<ReadAllCommentsResponse>> readAllComments(
            @RequestParam(required = false) String nickname) {
        List<ReadAllCommentsResponse> results = commentService.readAllComments(nickname);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(
            @PathVariable("scheduleId") Long scheduleId,
            @PathVariable("commentId") Long commentId,
            @RequestBody UpdateCommentRequest updateCommentRequest,
            @LoginUser SessionUser sessionUser) {
        UpdateCommentResponse result = commentService.updateComment(scheduleId, commentId, updateCommentRequest, sessionUser);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("scheduleId") Long scheduleId,
            @PathVariable("commentId") Long commentId,
            @LoginUser SessionUser sessionUser) {
        commentService.deleteComment(scheduleId, commentId, sessionUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
