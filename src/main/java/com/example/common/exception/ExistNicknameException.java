package com.example.common.exception;

public class ExistNicknameException extends CustomException{


    public ExistNicknameException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
