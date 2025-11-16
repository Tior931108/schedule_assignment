package com.example.common.exception;

public class ExistEmailException extends CustomException{

    public ExistEmailException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
