package com.example.common.exception;

import lombok.Getter;

@Getter
public class NotFoundUserException extends CustomException {


    public NotFoundUserException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
