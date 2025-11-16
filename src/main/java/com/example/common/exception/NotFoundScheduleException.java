package com.example.common.exception;

public class NotFoundScheduleException extends CustomException {

    public NotFoundScheduleException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
