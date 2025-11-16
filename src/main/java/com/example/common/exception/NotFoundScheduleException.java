package com.example.common.exception;

import lombok.Getter;

@Getter
public class NotFoundScheduleException extends CustomException {

    public NotFoundScheduleException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
