package com.example.common.exception;

public class ExistAndNewPasswordException extends CustomException{


    public ExistAndNewPasswordException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
