package com.example.common.exception;

public class OnlyOwnerAccessException extends CustomException {

    public OnlyOwnerAccessException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
