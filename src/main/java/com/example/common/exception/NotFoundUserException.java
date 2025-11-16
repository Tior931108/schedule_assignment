package com.example.common.exception;

public class NotFoundUserException extends CustomException {


    public NotFoundUserException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
