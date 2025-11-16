package com.example.common.exception;

import lombok.Getter;

@Getter
public class ExistNicknameException extends CustomException{


    public ExistNicknameException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
