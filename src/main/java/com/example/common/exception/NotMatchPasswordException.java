package com.example.common.exception;

public class NotMatchPasswordException extends CustomException {


    public NotMatchPasswordException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
