package com.example.common.exception;

import lombok.Getter;

@Getter
public class NotFoundCommentException extends CustomException {

    public NotFoundCommentException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
