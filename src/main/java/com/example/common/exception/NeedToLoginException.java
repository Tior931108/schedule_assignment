package com.example.common.exception;

public class NeedToLoginException extends CustomException{

    public NeedToLoginException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
