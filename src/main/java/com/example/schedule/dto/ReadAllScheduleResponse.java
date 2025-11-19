package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadAllScheduleResponse {

    private Long scheduleId;
    private String title;           // 할일 제목
    private String content;         // 할일 내용
    private Long commentCount;      // 댓글 개수
    private LocalDateTime createdAt; // 일정 작성일
    private LocalDateTime modifiedAt; // 일정 수정일
    private String nickname;         // 일정 작성 유저명


    // Object[] → ReadAllScheduleResponse 변환
    public static ReadAllScheduleResponse convertToDto(Object[] result) {
        return new ReadAllScheduleResponse(
                (Long) result[0],           // scheduleId
                (String) result[1],         // title
                (String) result[2],         // content
                (Long) result[3],           // commentCount
                (LocalDateTime) result[4],  // createdAt
                (LocalDateTime) result[5],  // modifiedAt
                (String) result[6]          // nickname
        );
    }

}
